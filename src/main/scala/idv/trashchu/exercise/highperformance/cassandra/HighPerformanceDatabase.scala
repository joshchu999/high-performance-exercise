package idv.trashchu.exercise.highperformance.cassandra

import com.typesafe.config.ConfigFactory
import com.websudos.phantom.connectors.ContactPoints
import com.websudos.phantom.dsl.{Database, KeySpaceDef}

import scala.collection.JavaConverters._

/**
  * Created by Joshchu on 2016/7/10.
  */
object Defaults {

  val config = ConfigFactory.load().getConfig("service.persistence")

  val hosts = config.getStringList("hosts").asScala //Seq("127.0.0.1")
  val connector = ContactPoints(hosts, config.getInt("port")).keySpace(config.getString("keyspace"))
}

class HighPerformanceDatabase(val keyspace: KeySpaceDef) extends Database(keyspace) {
  object users extends UserRepository with keyspace.Connector
  object monsters extends MonsterRepository with keyspace.Connector
  object pets extends PetRepository with keyspace.Connector
}

object HighPerformanceDatabase extends HighPerformanceDatabase(Defaults.connector)