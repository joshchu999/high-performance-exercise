package idv.trashchu.exercise.highperformance

import com.websudos.phantom.connectors.ContactPoints
import com.websudos.phantom.dsl.{Database, KeySpaceDef}
import idv.trashchu.exercise.highperformance.entity.ProfileRepository

/**
  * Created by Joshchu on 2016/7/10.
  */
object Defaults {

  val hosts = Seq("localhost")

  val connector = ContactPoints(hosts, 39042).keySpace("highPerformance")
}

class MyDatabase(val keyspace: KeySpaceDef) extends Database(keyspace) {
  object profiles extends ProfileRepository with keyspace.Connector
}

object MyDatabase extends MyDatabase(Defaults.connector)