package idv.trashchu.exercise.highperformance.service

import akka.actor.{ActorLogging, Props}
import com.typesafe.config.ConfigFactory
import idv.trashchu.exercise.highperformance.actor._
import idv.trashchu.exercise.highperformance.entity._

import scala.concurrent.duration._
import spray.routing.HttpServiceActor

/**
  * Created by Joshchu on 2016/7/8.
  */
class HighPerformanceHttpService extends HttpServiceActor with PerRequestCreator with ActorLogging {

  implicit def toFiniteDuration(d: java.time.Duration): FiniteDuration = Duration.fromNanos(d.toNanos)


  import context.dispatcher
  context.system.scheduler.schedule(1 millisecond, ConfigFactory.load().getDuration("service.monster.generate-interval"), context.actorOf(Props[GenerateMonsterActor]), GenerateMonsterEvent)


  def receive = runRoute {
    import idv.trashchu.exercise.highperformance.json.HighPerformanceJsonProtocol._
    import spray.httpx.SprayJsonSupport._

    pathPrefix("users" / JavaUUID) { userID =>
      pathEnd {
        put {
          entity(as[WalkRequestBody]) { requestBody =>
            ctx => perRequest(ctx, Props[WalkRequestActor], RequestBody.convert(userID, requestBody))
          }
        }
      } ~
      path("catch") {
        post {
          entity(as[CatchRequestBody]) { requestBody =>
            ctx => perRequest(ctx, Props[CatchRequestActor], RequestBody.convert(userID, requestBody))
          }
        }
      } ~
      path("pets") {
        get {
          ctx => perRequest(ctx, Props[ListRequestActor], RequestBody.convert(userID))
        }
      }
    }
  }
}