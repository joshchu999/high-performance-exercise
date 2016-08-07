package idv.trashchu.exercise.highperformance.entity

import java.util.UUID

/**
  * Created by Joshchu on 2016/7/23.
  */
sealed trait RequestEvent
case class WalkRequestEvent(userID: UUID, x: Int, y: Int) extends RequestEvent
case class CatchRequestEvent(userID: UUID, monsterID: UUID) extends RequestEvent
case class ListRequestEvent(userID: UUID) extends RequestEvent

sealed trait CassandraEvent
case class WriteUserSuccessEvent() extends CassandraEvent
case class WriteMonsterSuccessEvent() extends CassandraEvent
case class WritePetSuccessEvent() extends CassandraEvent
case class ReadMonsterByMonsterIDEvent(monsterID: UUID) extends CassandraEvent
case class ReadAllMonstersEvent() extends CassandraEvent
case class ReadPetsByUserIDEvent(userID: UUID) extends CassandraEvent
case class DeleteMonsterByMonsterIDEvent(monsterID: UUID) extends CassandraEvent

sealed trait ResponseEvent
case class WalkResponseEvent(code: Int, message: String, monsters: Seq[Monster]) extends ResponseEvent
case class CatchResponseEvent(code: Int, message: String) extends ResponseEvent
case class ListResponseEvent(code: Int, message: String, pets: Seq[Pet]) extends ResponseEvent

sealed trait ServiceEvent
case class GenerateMonsterEvent() extends ServiceEvent

sealed trait ClientEvent
case class StartEvent(clientID: Int) extends ClientEvent
case class WalkEvent(clientID: Int, userID: UUID, iteration: Int) extends ClientEvent
case class CatchEvent(clientID: Int, userID: UUID, monsterID: UUID, iteration: Int) extends ClientEvent
case class ListEvent(clientID: Int, userID: UUID, iteration: Int) extends ClientEvent
case class EndEvent(clientID: Int, times: Seq[Int]) extends ClientEvent
