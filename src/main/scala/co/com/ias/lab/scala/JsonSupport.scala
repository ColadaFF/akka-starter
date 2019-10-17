package co.com.ias.lab.scala

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import co.com.ias.lab.scala.UsersActor.{User, Users}
import spray.json.RootJsonFormat

trait JsonSupport extends SprayJsonSupport {

  import spray.json.DefaultJsonProtocol._

  implicit val userJsonFormat: RootJsonFormat[User] = jsonFormat2(User)
  implicit val usersJsonFormat: RootJsonFormat[Users] = jsonFormat1(Users)

}
