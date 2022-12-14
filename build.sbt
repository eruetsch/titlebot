import sbtcrossproject.{crossProject, CrossType}

enablePlugins(JavaAppPackaging)

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val server = (project in file("server")).settings(commonSettings).settings(
	name := "play-server",
  scalaJSProjects := Seq(client),
  pipelineStages in Assets := Seq(scalaJSPipeline),
  pipelineStages := Seq(digest, gzip),
  // triggers scalaJSPipeline when using compile or continuous compilation
  compile in Compile := ((compile in Compile) dependsOn scalaJSPipeline).value,
  libraryDependencies ++= Seq(
    "com.vmunier" %% "scalajs-scripts" % "1.1.4",
    guice,
		"org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test,
		"com.typesafe.play" %% "play-slick" % "5.0.0",
		"com.typesafe.slick" %% "slick-codegen" % "3.3.3",
    "org.postgresql" % "postgresql" % "42.2.18",
    "com.typesafe.slick" %% "slick-hikaricp" % "3.3.3",
    specs2 % Test,
    ws
  )
).enablePlugins(PlayScala)

lazy val client = (project in file("client")).settings(commonSettings).settings(
	name := "play-client",
  scalacOptions += "-Ymacro-annotations",
  scalaJSUseMainModuleInitializer := true,
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "1.1.0",
		"me.shadaj" %%% "slinky-core" % "0.6.6",
		"me.shadaj" %%% "slinky-web" % "0.6.6"  
    )
).enablePlugins(ScalaJSPlugin, ScalaJSWeb)

lazy val commonSettings = Seq(
  scalaVersion := "2.13.4",
  libraryDependencies += "com.typesafe.play" %%% "play-json" % "2.9.1",
)

// loads the server project at sbt startup
onLoad in Global := (onLoad in Global).value andThen {s: State => "project server" :: s}
