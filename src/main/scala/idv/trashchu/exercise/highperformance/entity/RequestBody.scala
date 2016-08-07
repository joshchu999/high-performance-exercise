package idv.trashchu.exercise.highperformance.entity

import java.util.UUID

/**
  * Created by Joshchu on 2016/7/23.
  */
sealed trait RequestBody
case class WalkRequestBody(x: Int, y: Int) extends RequestBody
case class CatchRequestBody(monsterID: UUID) extends RequestBody

object RequestBody {
  def convert(userID: UUID, requestBody: WalkRequestBody) = WalkRequestEvent(userID, requestBody.x, requestBody.y)
  def convert(userID: UUID, requestBody: CatchRequestBody) = CatchRequestEvent(userID, requestBody.monsterID)
  def convert(userID: UUID) = ListRequestEvent(userID)
}

