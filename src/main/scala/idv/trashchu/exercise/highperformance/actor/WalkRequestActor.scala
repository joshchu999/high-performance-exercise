package idv.trashchu.exercise.highperformance.actor

import akka.actor.{Actor, ActorLogging, Props}
import com.typesafe.config.ConfigFactory
import idv.trashchu.exercise.highperformance.entity._

/**
  * Created by Joshchu on 2016/7/23.
  */
class WalkRequestActor extends Actor with ActorLogging {

  var x: Int = -1
  var y: Int = -1

  def receive = {
    case WalkRequestEvent(userID, ex, ey) =>
      x = ex
      y = ey
      context.actorOf(Props[WriteCassandraActor]) ! User(userID, ex, ey)
    case WriteUserSuccessEvent() =>
      context.actorOf(Props[ReadCassandraActor]) ! ReadAllMonstersEvent()
    case monsters: Seq[Monster] =>
      import math._
      context.parent ! WalkResponseEvent(0, "Walk Success", monsters filter (monster => sqrt(pow(x - monster.x, 2) + pow(y - monster.y, 2)) <= ConfigFactory.load().getInt("service.user.vision")))
  }
}
