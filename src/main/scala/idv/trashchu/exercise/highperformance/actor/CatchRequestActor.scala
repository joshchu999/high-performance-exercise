package idv.trashchu.exercise.highperformance.actor

import akka.actor.{Actor, ActorLogging, Props}
import com.typesafe.config.ConfigFactory
import com.websudos.phantom.dsl.UUID
import idv.trashchu.exercise.highperformance.entity._

/**
  * Created by Joshchu on 2016/7/23.
  */
class CatchRequestActor extends Actor with ActorLogging {

  var userID: UUID = null
  var monsterID: UUID = null
  var categoryID: Int = -1

  def receive = {
    case CatchRequestEvent(uid, mid) =>

      if (scala.util.Random.nextDouble() > ConfigFactory.load().getDouble("service.monster.catch-rate")) {
        context.parent ! CatchResponseEvent(1, "Catch failure!")
      }
      else {
        userID = uid
        monsterID = mid

        context.actorOf(Props[ReadCassandraActor]) ! ReadMonsterByMonsterIDEvent(monsterID)
      }
    case Some(Monster(mid, cid, _, _)) =>
      categoryID = cid
      context.actorOf(Props[WriteCassandraActor]) ! DeleteMonsterByMonsterIDEvent(monsterID)
    case WriteMonsterSuccessEvent() =>
      context.actorOf(Props[WriteCassandraActor]) ! Pet(userID, monsterID, categoryID)
    case WritePetSuccessEvent() =>
      context.parent ! CatchResponseEvent(0, "Catch Success!!!!!!!!")
  }
}
