package idv.trashchu.exercise.highperformance

import akka.actor.ActorLogging
import spray.routing.HttpServiceActor

/**
  * Created by Joshchu on 2016/7/8.
  */
class HighPerformanceHttpService extends HttpServiceActor with ActorLogging {

  def receive = runRoute {
    import spray.httpx.SprayJsonSupport._
    import HighPerformanceJsonProtocol._

    path("") {
      get {
        complete("ok!")
      } ~
      post {
        entity(as[Profile]) { profile =>
          complete(profile)
        }
      }
    }
  }
}