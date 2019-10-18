package co.com.ias.lab.scala

import java.util.UUID
import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorLogging, Props}
import akka.pattern.pipe
import akka.util.Timeout
import co.com.ias.lab.scala.Quickstart.usersActor
import co.com.ias.lab.scala.UsersActor.ValidationType

import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}


object UsersActor {
  def props(dao: UsersDAO ): Props = Props(new UsersActor(dao))


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


  object ValidationType extends Enumeration {
    val USER_ALREADY_EXISTS, USER_DOES_NOT_EXISTS = Value
  }

  sealed trait DomainValidation {
    def reason: String

    def validationType: ValidationType.Value
  }

  case object UserAlreadyExists extends DomainValidation {
    val reason: String = "El usuario ya existe"
    val validationType: ValidationType.Value = ValidationType.USER_ALREADY_EXISTS
  }

  case object UserDoesNotExists extends DomainValidation {
    val reason: String = "El usuario no existe"
    val validationType: ValidationType.Value = ValidationType.USER_DOES_NOT_EXISTS
  }


}

class UsersActor(dao: UsersDAO) extends Actor with ActorLogging {
  import akka.pattern.pipe
  import UsersActor._

  implicit val ec:ExecutionContext = context.dispatcher
  implicit val timeout:Timeout = Timeout(5, TimeUnit.SECONDS)

  private val id: String = UUID.randomUUID().toString
  val users: mutable.Map[String, User] = scala.collection.mutable.Map[String, User](
    id -> User(id, "Test User")

  )

  def addUser(user: User): Either[DomainValidation, UserCreated] = {
    if (users.contains(user.id)) {
      Left(UserAlreadyExists)
    } else {
      users += (user.id -> user)
      Right(UserCreated(user))
    }
  }

  def updateUser(id: String, user: User): Either[DomainValidation, UserUpdated] = {
    if (users.contains(user.id)) {
      users += (user.id -> user)
      Right(UserUpdated(id, user))
    } else {
      Left(UserDoesNotExists)
    }
  }

  def findOne(id: String): Option[User] = users.get(id)

  def deleteOne(id: String): Either[DomainValidation, User] = {
    if (users.contains(id)) {
      val user = users(id)
      users -= id
      Right(user)
    } else {
      Left(UserDoesNotExists)
    }
  }


  override def receive: Receive = {
    case AddUser(user) =>
      sender() ! addUser(user)
    case FindAll => {
      log.info("Got Find All")
      val findAllFuture: Future[Seq[(Int, String, String)]] = dao.findAll
      findAllFuture
          .map(tuples => {
            val values = tuples
              .map(tuple => User(tuple._1.toString, s"${tuple._2} ${tuple._3} "))
            Users(values)
          })
        .pipeTo(sender())
    }
    case UpdateUser(id, user) => sender() ! updateUser(id, user)
    case FindOne(id) => sender() ! findOne(id)
    case DeleteUser(id) => sender() ! deleteOne(id)
  }
}
