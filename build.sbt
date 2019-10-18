
name := "akka-starter"

version := "0.1"

scalaVersion := "2.13.1"

lazy val akkaVersion = "2.5.26"
lazy val akkaHttpVersion = "10.1.10"
lazy val slickVersion = "3.3.2"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
  "org.scalatest" %% "scalatest" % "3.0.8" % Test,

  "com.typesafe.slick" %% "slick" % slickVersion,
  "com.typesafe.slick" %% "slick-hikaricp" % slickVersion,
  "org.slf4j" % "slf4j-nop" % "1.7.28",
  "com.typesafe" % "config" % "1.4.0",
  "com.h2database" % "h2" % "1.4.200"
)
