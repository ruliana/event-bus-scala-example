package ninenine

import org.joda.time.DateTime
import org.scalatest.{ BeforeAndAfterAll, FlatSpecLike, Matchers }

import akka.actor.ActorSystem
import akka.testkit.{ TestKit, TestProbe }

class EventStreamPoCSpec(_system: ActorSystem)
    extends TestKit(_system)
        with Matchers
        with FlatSpecLike
        with BeforeAndAfterAll {

  def this() = this(ActorSystem("EventBusPoCSpec"))

  override def afterAll: Unit = {
    shutdown(system)
  }

  "An EventStream" should "be a event bus" in {
    val probe1 = TestProbe()
    val probe2 = TestProbe()
    val probe3 = TestProbe()

    // Using akka default event bus
    val bus = system.eventStream

    bus.subscribe(probe1.ref, classOf[LoginEvent])
    bus.subscribe(probe2.ref, classOf[AuthenticationEvent])
    bus.subscribe(probe3.ref, classOf[LogoutEvent])

    val loginTime = new DateTime(1999, 12, 31, 23, 0)
    val logoutTime = new DateTime(2000, 1, 1, 1, 0)

    bus.publish(LoginEvent("1", loginTime))
    bus.publish(LogoutEvent("1", logoutTime))

    probe1.expectMsg(LoginEvent("1", loginTime))

    probe2.expectMsg(LoginEvent("1", loginTime))
    probe2.expectMsg(LogoutEvent("1", logoutTime))

    probe3.expectMsg(LogoutEvent("1", logoutTime))
  }
}