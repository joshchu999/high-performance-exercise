package idv.trashchu.exercise.highperformance.cassandra

import com.websudos.phantom.dsl._
import idv.trashchu.exercise.highperformance.entity.Pet

import scala.concurrent.Future

/**
  * Created by Joshchu on 2016/7/23.
  */
class Pets extends CassandraTable[PetRepository, Pet] {

  object userID extends UUIDColumn(this) with PartitionKey[UUID]
  object monsterID extends UUIDColumn(this) with PrimaryKey[UUID]
  object categoryID extends IntColumn(this)

  def fromRow(row: Row): Pet = {
    Pet(
      userID(row),
      monsterID(row),
      categoryID(row)
    )
  }
}

// The root connector comes from import com.websudos.phantom.dsl._
abstract class PetRepository extends Pets with RootConnector {

  def store(pet: Pet): Future[ResultSet] = {
    insert
      .value(_.userID, pet.userID)
      .value(_.monsterID, pet.monsterID)
      .value(_.categoryID, pet.categoryID)
      .consistencyLevel_=(ConsistencyLevel.ALL)
      .future()
  }

  def getByUserID(userID: UUID): Future[Seq[Pet]] = {
    select.where(_.userID eqs userID).fetch()
  }

  def getByUserIDAndMonsterID(userID: UUID, monsterID: UUID): Future[Option[Pet]] = {
    select.where(_.userID eqs userID).and(_.monsterID eqs monsterID).one()
  }
}