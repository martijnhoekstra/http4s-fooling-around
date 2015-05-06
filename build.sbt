name := "http4s techempower"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.6"

resolvers ++= Seq(
  "tpolecat" at "http://dl.bintray.com/tpolecat/maven",
  "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"
)

lazy val doobieversion = "0.2.2-SNAPSHOT" //publish-local from shapeless 2.2.0 branch; this is needed to not break stuff

lazy val http4sversion = "0.7.0"

libraryDependencies ++= Seq(
"org.http4s"   %% "http4s-dsl"                % http4sversion, 
"org.http4s"   %% "http4s-blazeserver"        % http4sversion, 
"org.http4s"   %% "http4s-json4s"             % http4sversion,     
"org.http4s"   %% "http4s-argonaut"           % http4sversion,   
"org.http4s"   %% "http4s-twirl"              % http4sversion,      
"org.tpolecat" %% "doobie-core"               % doobieversion,
"org.tpolecat" %% "doobie-contrib-postgresql" % doobieversion,
"io.argonaut"  %% "argonaut"                  % "6.1-M6",
"org.slf4j"     % "slf4j-simple"              % "1.7.7"
)

Revolver.settings

scalariformSettings

lazy val root = (project in file(".")).enablePlugins(SbtTwirl)