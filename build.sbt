name := "event-bus-sample"

version := "1.0"

scalaVersion := "2.12.2"

lazy val akkaVersion = "2.5.2"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "joda-time" % "joda-time" % "2.9.9" ,
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)
