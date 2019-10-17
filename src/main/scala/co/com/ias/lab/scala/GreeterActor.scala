package co.com.ias.lab.scala

import akka.actor.{Actor, ActorRef, Props}
import co.com.ias.lab.scala.LoggerActor.LogMessage

object GreeterActor {
  def props(message: String, loggerActor: ActorRef): Props =
    Props(new GreeterActor(message, loggerActor))

  case class Greeting(name: String)
}

class GreeterActor(message: String, loggerActor: ActorRef) extends Actor {
  import GreeterActor._


  override def unhandled(message: Any): Unit = {
    val stringMessage: String = s"${self.path.name} $message"
    loggerActor ! LogMessage(stringMessage)
  }

  override def receive: Receive = {
    case Greeting(name) => {
      val greetingMessage = s"$message $name"
      loggerActor ! greetingMessage
    }

  }
}
