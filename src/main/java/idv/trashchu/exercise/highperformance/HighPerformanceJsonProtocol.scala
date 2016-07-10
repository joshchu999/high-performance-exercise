package idv.trashchu.exercise.highperformance

import java.util.UUID

import spray.json.{DefaultJsonProtocol, DeserializationException, JsString, JsValue, JsonFormat, RootJsonFormat}

/**
  * Created by Joshchu on 2016/7/10.
  */
case class Profile(userID: UUID, profileID: UUID, name: String, age: Int)

object HighPerformanceJsonProtocol extends DefaultJsonProtocol {

  implicit object UuidJsonFormat extends JsonFormat[UUID] {
    def write(x: UUID) = JsString(x.toString)
    def read(value: JsValue) = value match {
      case JsString(x) => UUID.fromString(x)
      case x => throw new DeserializationException("Expected UUID as JsString, but got " + x)
    }
  }

  implicit val profileFormat = jsonFormat4(Profile)
}