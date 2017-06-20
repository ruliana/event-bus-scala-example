package ninenine

import org.joda.time.DateTime

trait AuthenticationEvent

sealed case class LoginEvent(id: String, time: DateTime) extends AuthenticationEvent
sealed case class LogoutEvent(id: String, time: DateTime) extends AuthenticationEvent

