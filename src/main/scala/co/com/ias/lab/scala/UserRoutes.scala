package co.com.ias.lab.scala

import java.util.concurrent.TimeUnit

import akka.actor.{ActorRef, ActorSystem}
import akka.pattern.ask
import akka.event.Logging
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import akka.http.scaladsl.server.directives.MethodDirectives.get
import akka.http.scaladsl.server.Directives._
import co.com.ias.lab.scala.UsersActor.{DomainValidation, FindAll, UserCreated, Users}

import scala.concurrent.Future
import scala.util.{Failure, Success}

trait UserRoutes extends JsonSupport {

  implicit val system: ActorSystem
  implicit lazy val timeout: Timeout = Timeout(5, TimeUnit.SECONDS)
  lazy val log = Logging(system, classOf[UserRoutes])

  def usersActor: ActorRef

  /**
   * localhost:8080/users/
   *  - GET
   *  - POST
   */

  /**
   * 1. desde el actor, crear usuario debe devolver un evento de error
   * en caso de que el usuario ya exista, controlar este caso.
   */

  lazy val userRoutes: Route =
    pathPrefix("users") {
      pathEnd {
        concat(
          get {
            log.info("on users")
            val users: Future[Users] = (usersActor ? FindAll)
              .mapTo[Users]
            complete(users)
          },
          post {
            entity(as[UsersActor.User]) { user => {
              log.info("Registering user ... ")
              val operation = (usersActor ? UsersActor.AddUser(user))
                .mapTo[Either[DomainValidation, UserCreated]]
              onSuccess(operation) {
                case Left(value) => complete((StatusCodes.BadRequest, value))
                case Right(value) => complete((StatusCodes.Created, value))
              }
            }
            }
          }
        )
      }
    }

}
