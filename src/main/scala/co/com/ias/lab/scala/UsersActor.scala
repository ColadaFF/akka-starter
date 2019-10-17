package co.com.ias.lab.scala

import java.util.UUID

import akka.actor.{Actor, ActorLogging, Props}
import akka.pattern.ask
import co.com.ias.lab.scala.Quickstart.usersActor

import scala.collection.mutable


object UsersActor {
  def props: Props = Props[UsersActor]


  case class User(id: String, name: String)
  case class Users(users: Seq[User])

  sealed trait UserOperationRequest
  case class AddUser(user: User) extends UserOperationRequest
  case class DeleteUser(userId: String) extends UserOperationRequest
  case class UpdateUser(userId: String, user: User) extends UserOperationRequest
  case class FindOne(userId: String) extends UserOperationRequest
  case object FindAll extends UserOperationRequest


  sealed trait UserOperationResponse
  sealed trait Success extends UserOperationResponse
  sealed trait Failure extends UserOperationResponse

  case class UserCreated(user: User) extends UserOperationResponse
  case class UserUpdated(userId: String, user: User) extends UserOperationResponse


}

class UsersActor extends Actor with ActorLogging {
  import UsersActor._

  private val id: String = UUID.randomUUID().toString
  val users: mutable.Map[String, User] = scala.collection.mutable.Map[String, User](
    id -> User(id, "Test User")

  )


  override def receive: Receive = {
    case AddUser(user) => {
      users + (user.id -> user)
      sender() ! UserCreated(user)
    }
    case FindAll => {
      log.info("Got Find All")
      sender() ! Users(users.values.toSeq)
    }
  }
}
