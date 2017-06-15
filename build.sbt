name := "akka-http-free-ser"

version := "1.0"

scalaVersion := "2.12.2"

lazy val akkaHttpVersion = "10.0.7"
lazy val akkaHttpJsonVersion = "1.16.1"
lazy val circeVersion = "0.8.0"
lazy val freestyleVersion = "0.3.0"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,
  "de.heikoseeberger" %% "akka-http-circe" % akkaHttpJsonVersion,
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  compilerPlugin("org.scalameta" % "paradise" % "3.0.0-M9" cross CrossVersion.patch),
  "org.specs2" %% "specs2-core" % "3.9.1" % Test
)

// freestyle
libraryDependencies += "io.frees" %% "freestyle" % freestyleVersion

// liberator
libraryDependencies += "io.aecor" %% "liberator" % "0.4.3"
