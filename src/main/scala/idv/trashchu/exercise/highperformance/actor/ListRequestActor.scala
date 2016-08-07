package idv.trashchu.exercise.highperformance.actor

import akka.actor.{Actor, ActorLogging, Props}
import idv.trashchu.exercise.highperformance.entity._

/**
  * Created by Joshchu on 2016/7/23.
  */
class ListRequestActor extends Actor with ActorLogging {

  def receive = {
    case ListRequestEvent(userID) =>
      context.actorOf(Props[ReadCassandraActor]) ! ReadPetsByUserIDEvent(userID)
    case pets: Seq[Pet] =>
      context.parent ! ListResponseEvent(0, "List Success", pets)
  }
}
