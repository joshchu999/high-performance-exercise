package idv.trashchu.exercise.highperformance.client

import akka.actor.{Actor, ActorLogging, Props}
import com.typesafe.config.ConfigFactory
import idv.trashchu.exercise.highperformance.entity._

import scala.concurrent.duration._

/**
  * Created by Joshchu on 2016/7/23.
  */
class HighPerformanceClient extends Actor with ActorLogging {

  def receive = {
    case StartEvent =>

      val clientNumber = ConfigFactory.load().getInt("client.number")
      val random = scala.util.Random

      import context.dispatcher

      for (i <- 1 to clientNumber) {
        context.system.scheduler.scheduleOnce(
          random.nextInt(clientNumber * 10) millisecond,
          context.actorOf(Props(classOf[UserActor], context.system)),
          StartEvent(i)
        )
      }

    case EndEvent =>
      context stop sender
  }
}
