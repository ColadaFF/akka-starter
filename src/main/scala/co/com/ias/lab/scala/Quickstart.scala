package co.com.ias.lab.scala

import java.util.concurrent.TimeUnit

import akka.actor.{ActorRef, ActorSystem}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

object Quickstart extends App {
  val system: ActorSystem = ActorSystem("akka-system-1")
  implicit val timeout:Timeout = Timeout(500, TimeUnit.MILLISECONDS)
  implicit val ec: ExecutionContext = ExecutionContext.global

  val loggerActor: ActorRef = system.actorOf(
    LoggerActor.props,
    "logger"
  )

  val morningGreeter: ActorRef = system.actorOf(
    GreeterActor.props("Buenos días", loggerActor),
    "morning-greeter"
  )

  val usersActor: ActorRef = system.actorOf(
    UsersActor.props,
    "users-actor"
  )


  private val future: Future[Any] = usersActor ? UsersActor.FindAll

  future
    .onComplete {
      case Failure(exception) => exception.printStackTrace()
      case Success(value) => println(s"value: $value")
    }

}
