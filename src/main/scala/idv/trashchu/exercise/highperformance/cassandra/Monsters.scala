package idv.trashchu.exercise.highperformance.cassandra

import com.websudos.phantom.dsl._
import idv.trashchu.exercise.highperformance.entity.Monster

import scala.concurrent.Future

/**
  * Created by Joshchu on 2016/7/23.
  */
class Monsters extends CassandraTable[MonsterRepository, Monster] {

  object monsterID extends UUIDColumn(this) with PartitionKey[UUID]
  object categoryID extends IntColumn(this)
  object x extends IntColumn(this)
  object y extends IntColumn(this)

  def fromRow(row: Row): Monster = {
    Monster(
      monsterID(row),
      categoryID(row),
      x(row),
      y(row)
    )
  }
}

// The root connector comes from import com.websudos.phantom.dsl._
abstract class MonsterRepository extends Monsters with RootConnector {

  def store(monster: Monster): Future[ResultSet] = {
    insert
      .value(_.monsterID, monster.monsterID)
      .value(_.categoryID, monster.categoryID)
      .value(_.x, monster.x)
      .value(_.y, monster.y)
      .consistencyLevel_=(ConsistencyLevel.ALL)
      .future()
  }

  def getByMonsterID(monsterID: UUID): Future[Option[Monster]] = {
    select.where(_.monsterID eqs monsterID).one()
  }

  def getAll: Future[Seq[Monster]] = {
    select.fetch
  }

  def removeByMonsterID(monsterID: UUID): Future[ResultSet] = {
    delete
      .where(_.monsterID eqs monsterID)
      .consistencyLevel_=(ConsistencyLevel.ALL)
      .future()
  }
}