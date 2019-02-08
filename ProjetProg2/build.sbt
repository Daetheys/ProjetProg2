val swing = "org.scala-lang.modules" %% "scala-swing" % "2.0.1"
lazy val root = (project in file(".")).
settings(
name := "ProjetProg2",
libraryDependencies += "org.scalafx" %% "scalafx" % "11-R16"
)