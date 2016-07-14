package idv.trashchu.exercise.highperformance

import java.util.UUID

import idv.trashchu.exercise.highperformance.entity.Profile
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

  implicit val profileFormat = jsonFormat4(Profile)
}