package ninenine

import akka.actor.ActorRef
import akka.event.EventBus
import akka.event.LookupClassification

final case class SimpleEvent(topic: String, payload: Any)

// It could be a simple class, however, we would need to
// pass it as parameter everywhere.
object EventBusPoC  extends EventBus with LookupClassification {
  override type Event = SimpleEvent
  override type Classifier = String
  override type Subscriber = ActorRef

  // Number of _different_ classifiers
  override protected def mapSize(): Int = 64

  // Same classifier == same topic.
  override protected def classify(event: Event): Classifier = event.topic

  // Subscribers must have an order
  override protected def compareSubscribers(a: Subscriber, b: Subscriber): Int = a compareTo b

  // Dispatch event to each subscriber, here we can decorate things
  override protected def publish(event: Event, subscriber: Subscriber): Unit = subscriber ! event.payload
}
