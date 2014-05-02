import sbt._
import Keys._

object AutoEcoleBuild extends Build {

  lazy val root = Project(
    id       = "autoecole",
    base     = file("."),
    settings = Root.settings
  ).aggregate(core, api)

  lazy val core = Project(
    id       = "autoecole-core",
    base     = file("core"),
    settings = Core.settings
  )

  lazy val api = Project(
    id       = "autoecole-api",
    base     = file("api"),
    settings = Api.settings
  )
}

object Versions {
  val slick = "2.1.0-M1"
  val spray = "1.3.1-20140423"
  val akka = "2.3.2"
}

object Dependencies {
  //val slick        = "com.typesafe.slick" %% "slick" % Versions.slick
  val slick        = "com.typesafe.slick" % "slick_2.11.0-RC4" % Versions.slick
  val akkaActors   = "com.typesafe.akka" %% "akka-actor" % Versions.akka
  val sprayRouting = "io.spray" %% "spray-routing" % Versions.spray
  val sprayCan     = "io.spray" %% "spray-can" % Versions.spray
}

object Common {
  
  def settings = Seq(
    version          := "1.0",
    scalaVersion     := "2.11.0",
    scalacOptions    := Seq("-unchecked", "-deprecation", "-encoding", "utf8", "-feature"),
    resolvers       ++= Seq(
      "spray repo" at "http://repo.spray.io",
      "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/"
    )
  )
}

object Packaging {
  import com.typesafe.sbt.packager.Keys._
  import com.typesafe.sbt.SbtNativePackager._

  def settings = packageArchetype.java_application ++ Seq(
    packageDescription  := "Autoecole.ch API application",
    maintainer          := "Thierry Monney <thiery.monney@gmail.com>"
  )
}

object Root {
  def settings = Project.defaultSettings ++
    Common.settings
}

object Core {
  import Dependencies._

  def dependencies = Seq(slick)
  
  def settings = Project.defaultSettings ++
    Common.settings ++
    Seq(libraryDependencies ++= dependencies)
}

object Api {
  import Dependencies._

  def dependencies = Seq(sprayRouting, sprayCan, akkaActors)
  
  def settings = Project.defaultSettings ++
    Common.settings ++
    Packaging.settings ++
    Seq(libraryDependencies ++= dependencies)
}