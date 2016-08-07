package idv.trashchu.exercise.highperformance.actor

import akka.actor.{Actor, ActorLogging}
import idv.trashchu.exercise.highperformance.cassandra.HighPerformanceDatabase
import idv.trashchu.exercise.highperformance.entity._

import scala.util.{Failure, Success}

/**
  * Created by Joshchu on 2016/7/23.
  */
class ReadCassandraActor extends Actor with ActorLogging {

  import context.dispatcher

  def receive = {
    case ReadMonsterByMonsterIDEvent(monsterID) =>
      val future = HighPerformanceDatabase.monsters.getByMonsterID(monsterID)
      future onComplete {
        case Success(monster) =>
          context.parent ! monster
        case Failure(result) =>
          log.error("Cassandra read monster failure: {}", result.getMessage)
      }
    case ReadAllMonstersEvent() =>
      val future = HighPerformanceDatabase.monsters.getAll
      future onComplete {
        case Success(monsters) =>
          context.parent ! monsters
        case Failure(result) =>
          log.error("Cassandra read monsters failure: {}", result.getMessage)
      }
    case ReadPetsByUserIDEvent(userID) =>
      val future = HighPerformanceDatabase.pets.getByUserID(userID)
      future onComplete {
        case Success(pets) =>
          context.parent ! pets
        case Failure(result) =>
          log.error("Cassandra read pets failure: {}", result.getMessage)
      }
  }
}
