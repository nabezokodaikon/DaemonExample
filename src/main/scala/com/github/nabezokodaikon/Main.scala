package com.github.nabezokodaikon

import com.typesafe.scalalogging.LazyLogging

object Main extends LazyLogging {

  def helloWorld(name: String): String = {
    "Hello " + name + "!"
  }

  def main(args: Array[String]): Unit = {
    val app = new AppDaemon("daemon-example")
    app.start()
  }

}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

import sun.misc.{ SignalHandler, Signal }
import org.apache.commons.daemon.{ Daemon, DaemonContext }
import akka.actor.{ Actor, ActorSystem, Props }

class AppDaemon(appName: String) extends Daemon with LazyLogging {

  def this() = {
    this("daemon-example")
  }

  private val system = ActorSystem(appName)
  private val actor = system.actorOf(Props[ProcessActor])

  override def init(context: DaemonContext) = {
    logger.info("init!!")
  }

  override def start() = {
    logger.info("start!!")

    actor ! CountDown(1)

    Signal.handle(new Signal("TERM"), new SignalHandler {
      def handle(sig: Signal) = {
        logger.info(sig.toString)
        stop()
      }
    })

  }

  override def stop() = {
    logger.info("stop!!")

    system.terminate()
    Await.result(system.whenTerminated, Duration.Inf)

    logger.info("stopped!!")
  }

  override def destroy() = {
    logger.info("destroy!!")
  }
}

case class CountDown(value: Int)

class ProcessActor extends Actor with LazyLogging {

  import scala.concurrent.duration._
  import scala.concurrent.ExecutionContext.Implicits.global

  def receive = {
    case CountDown(value) =>
      logger.info(s"${value}")
      context.system.scheduler.scheduleOnce(1.seconds, self, CountDown(value + 1))
  }
}
