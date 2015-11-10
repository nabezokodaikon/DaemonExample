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

  override def init(context: DaemonContext) = {
    logger.info("init!!")
  }

  override def start() = {
    logger.info("start!!")

    val actor = system.actorOf(Props[ProcessActor])
    actor ! CountDown(1)

    Signal.handle(new Signal("INT"), new SignalHandler {
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

  def receive = {
    case CountDown(value) =>
      Thread.sleep(1000)
      logger.info(s"${value}")
      self ! CountDown(value + 1)
  }
}
