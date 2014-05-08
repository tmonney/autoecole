import com.typesafe.sbt.packager.Keys._
//import com.typesafe.sbt.SbtNativePackager

import BuildSettings._
import Dependencies._

lazy val root = project in file(".") settings(rootSettings: _*) aggregate (core, api, web)

lazy val core = project settings(coreSettings: _*)

lazy val api = project settings(apiSettings: _*) dependsOn (core)

lazy val web = project settings(webSettings: _*) enablePlugins (SbtWeb)

lazy val rootSettings = baseSettings

lazy val coreSettings = baseSettings ++ Seq(
	libraryDependencies ++= Seq(slick)
)

lazy val apiSettings = baseSettings ++ packageArchetype.java_application ++ Seq(
	packageDescription  := "Autoecole.ch API application",
	maintainer          := "Thierry Monney <thiery.monney@gmail.com>",
	libraryDependencies ++= Seq(sprayRouting, sprayCan, akkaActors)    
)

lazy val webSettings = baseSettings ++ Seq(
	libraryDependencies ++= Seq(requirejs, underscorejs),
    pipelineStages := Seq(rjs, digest)
)