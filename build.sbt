lazy val commonSettings = Seq(
  organization := "com.github.nabezokodaikon",
  version := "0.0.1",
  scalaVersion := "2.11.7",
  scalacOptions ++= Seq(
    "-deprecation",
    "-feature",
    "-unchecked",
    "-Xlint"
  )
)

lazy val root = (project.in(file(".")))
  .settings(commonSettings: _*)
  .settings(
    name := "DaemonExample",
    libraryDependencies ++= Seq(
      // Test
      "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test",
      "org.scalacheck" %% "scalacheck" % "1.12.5" % "test",

      // Logger
      "com.typesafe.scala-logging" % "scala-logging_2.11" % "3.1.0",
      "ch.qos.logback" % "logback-classic" % "1.1.3",

      // Daemon
      "commons-daemon" % "commons-daemon" % "1.0.15",

      // Akka
      "com.typesafe.akka" % "akka-actor_2.11" % "2.4.0"
    )
  )

initialCommands := "import com.github.nabezokodaikon._"
