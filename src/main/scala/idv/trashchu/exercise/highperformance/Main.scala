package idv.trashchu.exercise.highperformance

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import idv.trashchu.exercise.highperformance.client.HighPerformanceClient
import idv.trashchu.exercise.highperformance.entity.StartEvent
import idv.trashchu.exercise.highperformance.service.HighPerformanceHttpService
import spray.can.Http

import scala.concurrent.duration._

/**
  * Created by Joshchu on 2016/7/8.
  */
object Main extends App {

  // we need an ActorSystem to host our application in
  implicit val system = ActorSystem("highPerformance")

  // create and start our service actor
  val service = system.actorOf(Props[HighPerformanceHttpService], "service")

  val client = system.actorOf(Props[HighPerformanceClient], "client")
  client ! StartEvent

  implicit val timeout = Timeout(10 seconds)
  // start a new HTTP server on port 8080 with our service actor as the handler
  IO(Http) ? Http.Bind(service, interface = "127.0.0.1", port = 8080)
}