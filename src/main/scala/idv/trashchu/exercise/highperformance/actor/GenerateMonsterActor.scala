package idv.trashchu.exercise.highperformance.actor

import akka.actor.{Actor, ActorLogging, Props}
import com.typesafe.config.ConfigFactory
import idv.trashchu.exercise.highperformance.entity._

/**
  * Created by Joshchu on 2016/7/23.
  */
class GenerateMonsterActor extends Actor with ActorLogging {

  def receive = {
    case GenerateMonsterEvent =>
      val config = ConfigFactory.load().getConfig("service")
      val random = scala.util.Random
      context.actorOf(Props[WriteCassandraActor]) ! Monster(java.util.UUID.randomUUID, random nextInt config.getInt("monster.categories"), random nextInt config.getInt("width"), random nextInt config.getInt("height"))
  }
}
