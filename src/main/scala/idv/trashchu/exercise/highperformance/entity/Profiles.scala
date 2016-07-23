package idv.trashchu.exercise.highperformance.entity

import com.websudos.phantom.dsl._

import scala.concurrent.Future

/**
  * Created by Joshchu on 2016/7/10.
  */
class Profiles extends CassandraTable[ProfileRepository, Profile] {

  object userID extends UUIDColumn(this) with PartitionKey[UUID]
  object profileID extends UUIDColumn(this)
  object name extends StringColumn(this)
  object age extends IntColumn(this)

  def fromRow(row: Row): Profile = {
    Profile(
      userID(row),
      profileID(row),
      name(row),
      age(row)
    )
  }
}

// The root connector comes from import com.websudos.phantom.dsl._
abstract class ProfileRepository extends Profiles with RootConnector {

  def store(profile: Profile): Future[ResultSet] = {
    insert
      .value(_.userID, profile.userID)
      .value(_.profileID, profile.profileID)
      .value(_.name, profile.name)
      .value(_.age, profile.age)
      .consistencyLevel_=(ConsistencyLevel.ALL)
      .future()
  }

  def getById(userID: UUID): Future[Option[Profile]] = {
    select.where(_.userID eqs userID).one()
  }
}