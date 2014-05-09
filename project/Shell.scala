import language.postfixOps

import sbt._
import Keys._

import complete._
import DefaultParsers._

object ShellSettings {

	lazy val sh = inputKey[Int]("Executes the given command in the underlying shell.")

	lazy val cmdParser: Parser[(String, Seq[String])] = {
		OptSpace ~> token(StringBasic) ~ (' ' ~> token(StringBasic)).*
	}

	lazy val shellSettings = Seq(
		sh := {
			val (cmd: String, args: Seq[String]) = cmdParser.parsed
			Process(Seq("sh", "-l", "-c", (cmd +: args).mkString(" "))) ! match {
				case exitCode if exitCode != 0 => sys.error(s"Failed with exit code $exitCode")
				case exitCode => exitCode
			}
		},

		aggregate in sh := false
	)

}