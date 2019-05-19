// Name of the project
name := "ScalaFX Hello World"

// Project version
version := "11-R16"

// Version of Scala used by the project
scalaVersion := "2.12.7"

// Add dependency on ScalaFX library
libraryDependencies += "org.scalafx" %% "scalafx" % "11-R16"
libraryDependencies += "org.platanios" % "tensorflow_2.12" % "0.4.0" classifier "linux-cpu-x86_64"
resolvers += Resolver.sonatypeRepo("snapshots")

scalacOptions ++= Seq("-unchecked", "-deprecation", "-Xcheckinit", "-encoding", "utf8", "-feature")


// Fork a new JVM for 'run' and 'test:run', to avoid JavaFX double initialization problems
fork := true

// Determine OS version of JavaFX binaries
lazy val osName = System.getProperty("os.name") match {
  case n if n.startsWith("Linux") => "linux"
  case n if n.startsWith("Mac") => "mac"
  case n if n.startsWith("Windows") => "win"
  case _ => throw new Exception("Unknown platform!")
}

// Add JavaFX dependencies
lazy val javaFXModules = Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
libraryDependencies ++= javaFXModules.map( m=>
  "org.openjfx" % s"javafx-$m" % "11" classifier osName
)