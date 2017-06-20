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
    val eventBus = new EventBusPoC

    // Register someone interested in the event
    eventBus.subscribe(testActor, "things-change")

    // Publish events
    eventBus.publish(SimpleEvent("time", System.currentTimeMillis()))
    eventBus.publish(SimpleEvent("things-change", "Hey, ho!"))

    // Ignores events we not subscribed for
    expectMsg("Hey, ho!")
  }

  "An EventBus" should "fan out events" in {
    val eventBus = new EventBusPoC

    // Several receivers
    val probe1 = TestProbe()
    val probe2 = TestProbe()
    val probe3 = TestProbe()

    // Not everybody is interested in everything
    eventBus.subscribe(probe1.ref, "login")
    eventBus.subscribe(probe2.ref, "login")
    eventBus.subscribe(probe2.ref, "logout")
    eventBus.subscribe(probe3.ref, "logout")

    // Just two events
    eventBus.publish(SimpleEvent("login", "Yo!"))
    eventBus.publish(SimpleEvent("logout", "See ya!"))

    // Everybody gets what they asked
    probe1.expectMsg("Yo!")
    probe2.expectMsg("Yo!")
    probe2.expectMsg("See ya!")
    probe3.expectMsg("See ya!")
  }
}