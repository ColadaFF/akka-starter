package co.com.ias.lab.scala

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}

object Quickstart extends App with UserRoutes with DatabaseConfig {
  implicit val system: ActorSystem = ActorSystem("akka-system-1")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  override implicit val ec: ExecutionContext = system.dispatcher



  val usersActor: ActorRef = system.actorOf(
    UsersActor.props,
    "users-actor"
  )


  lazy val routes: Route = userRoutes

  val serverBinding: Future[Http.ServerBinding] = Http()
    .bindAndHandle(routes, "localhost", 8080)

  serverBinding.onComplete {
    case Failure(exception) =>
      Console.err.println("Error en el servidor")
      exception.printStackTrace()
      system.terminate()
    case Success(value) =>
      val serverAddress = value.localAddress.getHostString
      val serverPort = value.localAddress.getPort
      println(s"Servidor corriendo en http://$serverAddress:$serverPort")
      setupDb()
  }

  Await.result(system.whenTerminated, Duration.Inf)
}

//https://github.com/coladaff/akka-starter
