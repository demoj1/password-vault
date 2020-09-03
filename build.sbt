name := "password-vault"
version := "0.1"
scalaVersion := "2.13.3"
mainClass := Some("vault.Main")

val ScalatraVersion = "2.7.+"

libraryDependencies ++= Seq(
  "org.scalatra" %% "scalatra" % ScalatraVersion,
  "org.scalatra" %% "scalatra-scalatest" % ScalatraVersion % "test",
  "org.scalatra" %% "scalatra-auth" % ScalatraVersion,
  "ch.qos.logback" % "logback-classic" % "1.2.3" % "runtime",
  "org.eclipse.jetty" % "jetty-webapp" % "9.4.28.v20200408" % "container;compile;provided",
  "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided",
  "com.storm-enroute" %% "scalameter-core" % "0.19"
)

testFrameworks += new TestFramework("org.scalameter.ScalaMeterFramework")
logBuffered := false

val circeVersion = "0.12.0"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser",
  "io.circe" %% "circe-yaml"
).map(_ % circeVersion)

javaOptions ++= Seq(
  "-Xdebug",
  "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
)

enablePlugins(SbtTwirl)
enablePlugins(ScalatraPlugin)
