package idv.trashchu.exercise.highperformance.cassandra

import com.websudos.phantom.dsl._
import idv.trashchu.exercise.highperformance.entity.User

import scala.concurrent.Future

/**
  * Created by Joshchu on 2016/7/23.
  */
class Users extends CassandraTable[UserRepository, User] {

  object userID extends UUIDColumn(this) with PartitionKey[UUID]
  object x extends IntColumn(this)
  object y extends IntColumn(this)

  def fromRow(row: Row): User = {
    User(
      userID(row),
      x(row),
      y(row)
    )
  }
}

// The root connector comes from import com.websudos.phantom.dsl._
abstract class UserRepository extends Users with RootConnector {

  def store(user: User): Future[ResultSet] = {
    insert
      .value(_.userID, user.userID)
      .value(_.x, user.x)
      .value(_.y, user.y)
      .consistencyLevel_=(ConsistencyLevel.ALL)
      .future()
  }

  def getByUserID(userID: UUID): Future[Option[User]] = {
    select.where(_.userID eqs userID).one()
  }
}