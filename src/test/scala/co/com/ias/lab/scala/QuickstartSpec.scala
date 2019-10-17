package co.com.ias.lab.scala

import akka.actor.ActorSystem
import akka.testkit.{TestKit, TestProbe}
import co.com.ias.lab.scala.GreeterActor.Greeting
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import scala.concurrent.duration._
import scala.language.postfixOps

class QuickstartSpec(_system: ActorSystem)
  extends TestKit(_system)
  with Matchers
  with WordSpecLike
  with BeforeAndAfterAll {

  def this() = this(ActorSystem("test-actor-system"))

  override def afterAll(): Unit = {
    shutdown(_system)
  }

  "Un Greeter Actor" should {
    "devolver un mensaje de saludo cuando se le envie la orden" in {
      val testProbe = TestProbe()

      val initialMessage = "Hola"
      val props = GreeterActor.props(initialMessage, testProbe.ref)
      val greeterActor = system.actorOf(props)
      val greetPerson = "Camilo"

      greeterActor ! Greeting(greetPerson)
      val expectedMessage = s"$initialMessage $greetPerson"
      testProbe.expectMsg(500 millis, expectedMessage)
    }
  }

}
