package idv.trashchu.exercise.highperformance.actor

import akka.actor.{Actor, ActorLogging}
import idv.trashchu.exercise.highperformance.cassandra.HighPerformanceDatabase
import idv.trashchu.exercise.highperformance.entity._

import scala.util.{Failure, Success}

/**
  * Created by Joshchu on 2016/7/23.
  */
class WriteCassandraActor extends Actor with ActorLogging {

  import context.dispatcher

  def receive = {
    case user: User =>
      val future = HighPerformanceDatabase.users.store(user)
      future onComplete {
        case Success(_) =>
          context.parent ! WriteUserSuccessEvent()
        case Failure(result) =>
          log.error("Cassandra write user failure: {}", result.getMessage)
      }
    case monster: Monster =>
      val future = HighPerformanceDatabase.monsters.store(monster)
      future onComplete {
        case Success(_) =>
          context.parent ! WriteMonsterSuccessEvent()
        case Failure(result) =>
          log.error("Cassandra write monster failure: {}", result.getMessage)
      }
    case pet: Pet =>
      val future = HighPerformanceDatabase.pets.store(pet)
      future onComplete {
        case Success(_) =>
          context.parent ! WritePetSuccessEvent()
        case Failure(result) =>
          log.error("Cassandra write pet failure: {}", result.getMessage)
      }
    case DeleteMonsterByMonsterIDEvent(monsterID) =>
      val future = HighPerformanceDatabase.monsters.removeByMonsterID(monsterID)
      future onComplete {
        case Success(_) =>
          context.parent ! WriteMonsterSuccessEvent()
        case Failure(result) =>
          log.error("Cassandra write monster failure: {}", result.getMessage)
      }
  }
}
