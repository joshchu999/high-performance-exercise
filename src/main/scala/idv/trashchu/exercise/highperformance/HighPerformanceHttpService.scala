package idv.trashchu.exercise.highperformance

import akka.actor.ActorLogging
import idv.trashchu.exercise.highperformance.entity.Profile
import spray.routing.HttpServiceActor

/**
  * Created by Joshchu on 2016/7/8.
  */
class HighPerformanceHttpService extends HttpServiceActor with ActorLogging {

  def receive = runRoute {
    import HighPerformanceJsonProtocol._
    import spray.httpx.SprayJsonSupport._

    path("") {
      get {
        complete("ok!")
      } ~
      post {
        entity(as[Profile]) { profile =>
//          complete(profile)
          MyDatabase.profiles.store(profile)
          complete(profile)
        }
      }
    }
  }
}