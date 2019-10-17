package co.com.ias.lab.scala

import akka.actor.{Actor, ActorLogging, Props}

object LoggerActor {
  def props: Props = Props[LoggerActor]

  case class LogMessage(message: String)
}

class LoggerActor extends Actor with ActorLogging {
  import LoggerActor._
  override def receive: Receive = {
    case LogMessage(message) => {
      log.info(s"Unhandled message $message")
    }
  }
}
