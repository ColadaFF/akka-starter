package co.com.ias.lab.scala

import java.util.concurrent.TimeUnit

import akka.actor.{ActorRef, ActorSystem}
import akka.pattern.ask
import akka.event.Logging
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import akka.http.scaladsl.server.directives.MethodDirectives.get
import akka.http.scaladsl.server.Directives._
import co.com.ias.lab.scala.UsersActor.{FindAll, Users}

import scala.concurrent.Future

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

  lazy val userRoutes: Route =
    pathPrefix("users") {
      pathEnd {
        get {
          log.info("on users")
          val users: Future[Users] = (usersActor ? FindAll)
            .mapTo[Users]
          complete(users)
        }
      }
    }

}
