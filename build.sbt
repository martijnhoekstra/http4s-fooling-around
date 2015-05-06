name := "http4s techempower"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.6"

resolvers ++= Seq(
  "tpolecat" at "http://dl.bintray.com/tpolecat/maven",
  "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"
)

libraryDependencies ++= Seq(
"org.http4s" %% "http4s-dsl"          % "0.7.0", 
"org.http4s" %% "http4s-blazeserver"  % "0.7.0", 
"org.http4s" %% "http4s-json4s" % "0.7.0",     
"org.http4s" %% "http4s-argonaut" % "0.7.0",   
"org.http4s" %% "http4s-twirl" % "0.7.0",      
"org.tpolecat" %% "doobie-core" % "0.2.1",
"io.argonaut" %% "argonaut" % "6.1-M6",
"org.slf4j" % "slf4j-simple" % "1.7.7"
)

Revolver.settings

scalariformSettings

lazy val root = (project in file(".")).enablePlugins(SbtTwirl)