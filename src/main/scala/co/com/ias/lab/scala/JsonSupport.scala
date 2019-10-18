package co.com.ias.lab.scala

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.util.Collections
import co.com.ias.lab.scala.UsersActor.{DomainValidation, User, UserAlreadyExists, UserCreated, Users, ValidationType}
import spray.json.{DeserializationException, JsObject, JsString, JsValue, RootJsonFormat}

import scala.collection.immutable.{AbstractSeq, LinearSeq}

trait JsonSupport extends SprayJsonSupport {

  import spray.json.DefaultJsonProtocol._

  implicit val userJsonFormat: RootJsonFormat[User] = jsonFormat2(User)
  implicit val userCreatedFormat: RootJsonFormat[UserCreated] = jsonFormat1(UserCreated)
  implicit val usersJsonFormat: RootJsonFormat[Users] = jsonFormat1(Users)

  /**
   * {"validationType": "", "reason": ""}
   */

  implicit object UserDomainValidationFormat extends RootJsonFormat[DomainValidation] {
    override def read(json: JsValue): DomainValidation = {
      json.asJsObject.getFields("validationType", "reason") match {
        case Seq(JsString(validationType), JsString(_)) => {
          validationType match {
            case "USER_ALREADY_EXISTS" => UserAlreadyExists
            case _ => throw DeserializationException("unknown validationType expected")
          }
        }
        case _ => throw DeserializationException("DomainValidation expected")
      }
    }

    override def write(obj: DomainValidation): JsValue = {
      JsObject(
        "validationType" -> JsString(obj.validationType.toString),
        "reason" -> JsString(obj.reason)
      )
    }
  }

}
