package ch.autoecole.api

import akka.actor._
import akka.io._
import java.net.InetSocketAddress
import spray.can._
import spray.routing._
import spray.http._

object Main extends App {
	implicit val system = ActorSystem()

	val handler = system.actorOf(Props[ExampleServiceActor], name = "example-service")
  	IO(Http) ! Http.Bind(handler, interface = "0.0.0.0", port = 8080)
}

class ExampleServiceActor extends Actor with ExampleService {
  def actorRefFactory = context
  def receive = runRoute(route)
}

trait ExampleService extends HttpService {
  import MediaTypes._
  
  val route =
    path("") {
      get {
        respondWithMediaType(`text/html`) {
          complete {
            <html>
              <body>
                <h1>Hello World!</h1>
                <p>Database host: <pre>{sys.env("DB_PORT_5432_TCP_ADDR")}:{sys.env("DB_PORT_5432_TCP_PORT")}</pre></p>
              </body>
            </html>
          }
        }
      }
    }
}