package ninenine

import org.scalatest.{ BeforeAndAfterAll, FlatSpecLike, Matchers }

import akka.actor.ActorSystem
import akka.testkit.{ TestKit, TestProbe }

class EventBusPoCSpec(_system: ActorSystem)
    extends TestKit(_system)
        with Matchers
        with FlatSpecLike
        with BeforeAndAfterAll {

  def this() = this(ActorSystem("EventBusPoCSpec"))

  override def afterAll: Unit = {
    shutdown(system)
  }

  "An EventBus" should "works more or less like an observer" in {
    // Register someone interested in the event
    EventBusPoC.subscribe(testActor, "things-change")

    // Publish events
    EventBusPoC.publish(SimpleEvent("time", System.currentTimeMillis()))
    EventBusPoC.publish(SimpleEvent("things-change", "Hey, ho!"))

    // Ignores events we not subscribed for
    expectMsg("Hey, ho!")
  }

  "An EventBus" should "fan out events" in {
    // Several receivers
    val probe1 = TestProbe()
    val probe2 = TestProbe()
    val probe3 = TestProbe()

    // Not everybody is interested in everything
    EventBusPoC.subscribe(probe1.ref, "login")
    EventBusPoC.subscribe(probe2.ref, "login")
    EventBusPoC.subscribe(probe2.ref, "logout")
    EventBusPoC.subscribe(probe3.ref, "logout")

    // Just two events
    EventBusPoC.publish(SimpleEvent("login", "Yo!"))
    EventBusPoC.publish(SimpleEvent("logout", "See ya!"))

    // Everybody gets what they asked
    probe1.expectMsg("Yo!")
    probe2.expectMsg("Yo!")
    probe2.expectMsg("See ya!")
    probe3.expectMsg("See ya!")
  }
}