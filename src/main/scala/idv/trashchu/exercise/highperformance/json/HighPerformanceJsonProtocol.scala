package idv.trashchu.exercise.highperformance.json

import java.util.UUID

import idv.trashchu.exercise.highperformance.entity._
import spray.json._

/**
  * Created by Joshchu on 2016/7/10.
  */
object HighPerformanceJsonProtocol extends DefaultJsonProtocol {

  implicit object UuidJsonFormat extends JsonFormat[UUID] {
    def write(x: UUID) = JsString(x.toString)
    def read(value: JsValue) = value match {
      case JsString(x) => UUID.fromString(x)
      case x => deserializationError("Expected UUID as JsString, but got " + x)
    }
  }

  implicit val walkRequestBodyFormat = jsonFormat2(WalkRequestBody)
  implicit val catchRequestBodyFormat = jsonFormat1(CatchRequestBody)

  implicit val walkMonsterResponseBodyFormat = jsonFormat4(WalkMonsterResponseBody)
  implicit val walkResponseBodyFormat = jsonFormat3(WalkResponseBody)
  implicit val catchResponseBodyFormat = jsonFormat2(CatchResponseBody)
  implicit val listPetResponseBodyFormat = jsonFormat2(ListPetResponseBody)
  implicit val listResponseBodyFormat = jsonFormat3(ListResponseBody)
}