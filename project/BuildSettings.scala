import sbt._
import Keys._

object BuildSettings {
	val buildOrganization = "ch.autoecole"
	val buildVersion = "1.0"
	val buildScalaVersion = "2.11.0"

	def baseSettings = Defaults.coreDefaultSettings ++ ShellSettings.shellSettings ++ Seq(
		organization     := buildOrganization,
		version          := buildVersion,
		scalaVersion     := buildScalaVersion,
		scalacOptions    := Seq("-unchecked", "-deprecation", "-encoding", "utf8", "-feature"),
		shellPrompt      := ShellPrompt.buildShellPrompt,
		resolvers       ++= Seq(
      		"spray repo" at "http://repo.spray.io",
      		"Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/"
    	)
	)
}
