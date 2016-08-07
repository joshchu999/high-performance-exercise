package idv.trashchu.exercise.highperformance.client

import java.util.UUID

import akka.actor.{Actor, ActorLogging, ActorSystem}
import com.typesafe.config.ConfigFactory
import idv.trashchu.exercise.highperformance.entity._
import spray.http.HttpRequest
import spray.client.pipelining._

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.{Failure, Success}

/**
  * Created by Joshchu on 2016/7/23.
  */
class UserActor(implicit system: ActorSystem) extends Actor with ActorLogging {

  implicit def toFiniteDuration(d: java.time.Duration): FiniteDuration = Duration.fromNanos(d.toNanos)

  import context.dispatcher
  import idv.trashchu.exercise.highperformance.json.HighPerformanceJsonProtocol._
  import spray.httpx.SprayJsonSupport._

  val config = ConfigFactory.load()
  val serviceConfig = config.getConfig("service")
  val clientConfig = config.getConfig("client")
  val serviceURL = clientConfig.getString("service-url")
  val random = scala.util.Random

  def walk(userID: UUID) = {
    val pipeline: HttpRequest => Future[WalkResponseBody] = (
      sendReceive
        ~> unmarshal[WalkResponseBody]
      )

    pipeline(Put(s"${serviceURL}/users/${userID}", WalkRequestBody(random nextInt serviceConfig.getInt("width"), random nextInt serviceConfig.getInt("height"))))
  }

  def `catch`(userID: UUID, monsterID: UUID) = {
    val pipeline: HttpRequest => Future[CatchResponseBody] = (
      sendReceive
        ~> unmarshal[CatchResponseBody]
      )

    pipeline(Post(s"${serviceURL}/users/${userID}/catch", CatchRequestBody(monsterID)))
  }

  def list(userID: UUID) = {
    val pipeline: HttpRequest => Future[ListResponseBody] = (
      sendReceive
        ~> unmarshal[ListResponseBody]
      )

    pipeline(Get(s"${serviceURL}/users/${userID}/pets"))
  }

  def receive = {

    case StartEvent(clientID) =>
      log.info("{}: Staring...", clientID)
      val userID = java.util.UUID.randomUUID
      self ! WalkEvent(clientID, userID, 1)

    case WalkEvent(clientID, userID, iteration) =>
      val response = walk(userID)
      response onComplete {
        case Success(responseBody) =>
          log.debug("{}: Walk Response: {}", clientID, responseBody)

          val monsters = responseBody.monsters
          val monsterLength = monsters.length
          if (monsterLength > 0) {
            context.system.scheduler.scheduleOnce(3 second, self, CatchEvent(clientID, userID, monsters(random.nextInt(monsterLength)).monsterID, iteration))
          }
          else {
            context.system.scheduler.scheduleOnce(3 second, self, ListEvent(clientID, userID, iteration))
          }
        case Failure(result) =>
          log.error("{}: Walk failure: {}", clientID, result.getMessage)
      }

    case CatchEvent(clientID, userID, monsterID, iteration) =>
      val response = `catch`(userID, monsterID)
      response onComplete {
        case Success(responseBody) =>
          log.debug("{}: Catch Response: {}", clientID, responseBody)

          context.system.scheduler.scheduleOnce(5 second, self, ListEvent(clientID, userID, iteration))
        case Failure(result) =>
          log.error("{}: Catch failure: {}", clientID, result.getMessage)
      }

    case ListEvent(clientID, userID, iteration) =>
      val response = list(userID)
      response onComplete {
        case Success(responseBody) =>
          log.debug("{}: List Response: {}", clientID, responseBody)

          if (iteration == clientConfig.getInt("iteration")) {
            self ! EndEvent(clientID, Nil)
          }
          else {
            context.system.scheduler.scheduleOnce(3 second, self, WalkEvent(clientID, userID, iteration + 1))
          }
        case Failure(result) =>
          log.info("{}: List failure: {}", clientID, result.getMessage)
      }

    case EndEvent(clientID, times) =>
      log.info("{}: Finished", clientID)
      context.parent ! EndEvent
  }
}
