import sbt._
import Keys._

import language.postfixOps

// Shell prompt which show the current project,
// git branch and build version
object ShellPrompt {
  object devnull extends ProcessLogger {
    def info(s: => String) {}
    def error(s: => String) {}
    def buffer[T](f: => T): T = f
  }

  import scala.Console.{ RESET => reset }
  val red = "\033[0;31m"
  val green = "\033[0;32m"
  val yellow = "\033[0;33m"
  val blue = "\033[1;34m"
  val magenta = "\033[0;35m"
  val cyan = "\033[0;36m"
  
  def colorize(text: String, color: String) = {
    s"${color}${text}${reset}"
  }

  def headLabel(label: String) = {
    val ref: String = "git symbolic-ref -q HEAD" !!
    val cmd: String = s"git for-each-ref --format=%(${label}:short) ${ref}"
    Option((cmd !!).stripLineEnd) filter (_.trim.nonEmpty) getOrElse "<none>"
  }

  val buildShellPrompt = {
    (state: State) =>
      {
        val sbt = colorize("sbt:", yellow)
        val projectId = Project.extract(state).currentProject.id
        val version = BuildSettings.buildVersion
        val project = colorize(s"${projectId}-${version}", blue)
        val branch = colorize(headLabel("refname"), green)
        val upstream = colorize(headLabel("upstream"), cyan)
        

        s"${sbt}${project} [${branch}->${upstream}]: "
      }
  }
}