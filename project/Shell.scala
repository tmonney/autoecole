import sbt._
import Keys._

import language.postfixOps

object ShellSettings {

	lazy val sh = inputKey[Int]("Executes the given command in the underlying shell.")

	lazy val shellSettings = Seq(
		sh := {
			val args: Seq[String] = Def.spaceDelimited("<command>").parsed

			Process(Seq("sh", "-l", "-c", args.mkString(" "))) ! match {
				case exitCode if exitCode != 0 => sys.error(s"Failed with exit code $exitCode")
				case exitCode => exitCode
			}
		},

		aggregate in sh := false
	)

}