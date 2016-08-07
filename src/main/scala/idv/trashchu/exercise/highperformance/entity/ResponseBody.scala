package idv.trashchu.exercise.highperformance.entity

import java.util.UUID

/**
  * Created by Joshchu on 2016/7/23.
  */
sealed trait ResponseBody
case class WalkMonsterResponseBody(monsterID: UUID, categoryID: Int, x: Int, y: Int) extends ResponseBody
case class WalkResponseBody(code: Int, message: String, monsters: Seq[WalkMonsterResponseBody]) extends ResponseBody
case class CatchResponseBody(code: Int, message: String) extends ResponseBody
case class ListPetResponseBody(monsterID: UUID, categoryID: Int) extends ResponseBody
case class ListResponseBody(code: Int, message: String, pets: Seq[ListPetResponseBody]) extends ResponseBody

object ResponseBody {
  def convert(event: WalkResponseEvent) = WalkResponseBody(event.code, event.message, event.monsters.map(m => WalkMonsterResponseBody(m.monsterID, m.categoryID, m.x, m.y)))
  def convert(event: CatchResponseEvent) = CatchResponseBody(event.code, event.message)
  def convert(event: ListResponseEvent) = ListResponseBody(event.code, event.message, event.pets.map(m => ListPetResponseBody(m.monsterID, m.categoryID)))
}
