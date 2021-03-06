val Http4sVersion = "0.21.22"
val CirceVersion = "0.13.0"
val Datadog4sVersion = "0.12.1"
val MunitVersion = "0.7.25"
val LogbackVersion = "1.2.3"
val MunitCatsEffectVersion = "0.13.0"

lazy val root = (project in file("."))
  .settings(
    organization := "io.pig",
    name := "http4swithdatadog",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.13.5",
    libraryDependencies ++= Seq(
      "org.http4s"      %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s"      %% "http4s-blaze-client" % Http4sVersion,
      "org.http4s"      %% "http4s-circe"        % Http4sVersion,
      "org.http4s"      %% "http4s-dsl"          % Http4sVersion,
      "io.circe"        %% "circe-generic"       % CirceVersion,
      "com.avast.cloud" %% "datadog4s"           % Datadog4sVersion,
      "com.avast.cloud" %% "datadog4s-http4s"     % Datadog4sVersion,
      "org.scalameta"   %% "munit"               % MunitVersion           % Test,
      "org.typelevel"   %% "munit-cats-effect-2" % MunitCatsEffectVersion % Test,
      "ch.qos.logback"  %  "logback-classic"     % LogbackVersion,
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.10.3"),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1"),
    testFrameworks += new TestFramework("munit.Framework")
  )
