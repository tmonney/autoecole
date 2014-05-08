import sbt._
import Keys._

object Versions {
  val slick = "2.1.0-M1"
  val spray = "1.3.1-20140423"
  val akka = "2.3.2"

  val requirejs = "2.1.11-1"
  val underscorejs = "1.6.0-1"
}

object Dependencies {
  //val slick        = "com.typesafe.slick" %% "slick" % Versions.slick
  val slick        = "com.typesafe.slick" % "slick_2.11.0-RC4" % Versions.slick
  val akkaActors   = "com.typesafe.akka" %% "akka-actor" % Versions.akka
  val sprayRouting = "io.spray" %% "spray-routing" % Versions.spray
  val sprayCan     = "io.spray" %% "spray-can" % Versions.spray

  val requirejs    = "org.webjars" % "requirejs" % Versions.requirejs
  val underscorejs  = "org.webjars" % "underscorejs" % Versions.underscorejs
}