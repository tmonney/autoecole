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

  sealed trait Ref
  case class LocalBranch(local: String) extends Ref
  case class TrackedBranch(local: String, upstream: String) extends Ref
  case class DetachedHead(sha1: String) extends Ref

  def isGitRepo = {
    ("git rev-parse --git-dir" ! devnull) == 0
  }

  def currentRef = {
    ("git rev-parse --symbolic-full-name HEAD" !!).trim match {
      case "HEAD" => DetachedHead(("git rev-parse --short HEAD" !!).stripLineEnd)
      case ref => Seq("refname", "upstream") map (refLabel(ref)) match {
        case Seq(Some(branch), Some(upstream)) => TrackedBranch(branch, upstream)
        case Seq(Some(branch), None) => LocalBranch(branch)
        case _ => sys.error("Unexpected git state")
      }
    }
  }

  def workingDirDirty = {
    ("git diff --quiet" ! devnull) != 0
  }

  def indexDirty = {
    ("git diff --quiet --cached" ! devnull) != 0
  }

  def colorize(text: String, color: String) = {
    s"${color}${text}${reset}"
  }

  def refLabel(ref: String)(label: String): Option[String] = {
    val cmd: String = s"git for-each-ref --format=%(${label}:short) ${ref}"
    Option((cmd !!).trim) filter (_.nonEmpty)
  }

  def colorizeBranch(branch: String) =
    colorize(branch, if (workingDirDirty) red else if (indexDirty) yellow else green)

  def branchInfo = currentRef match {
    case DetachedHead(sha1) => s"""${colorize("DETACHED at ", magenta)}${colorizeBranch(sha1)}"""
    case LocalBranch(branch) => colorizeBranch(branch)
    case TrackedBranch(branch, upstream) => s"${colorizeBranch(branch)}->${colorize(upstream, cyan)}"
  }

  val buildShellPrompt = {
    (state: State) =>
      {
        val sbt = colorize("sbt:", magenta)
        val projectId = Project.extract(state).currentProject.id
        val version = BuildSettings.buildVersion
        val project = colorize(s"${projectId}-${version}", blue)
        s"${sbt}${project} [${branchInfo}]> "
      }
  }
}