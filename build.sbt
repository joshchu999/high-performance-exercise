name := "high-performance-exercise"

version := "1.0"

scalaVersion := "2.11.8"

resolvers ++= Seq(
  "Typesafe repository releases" at "http://repo.typesafe.com/typesafe/releases/",
  "Sonatype releases" at "https://oss.sonatype.org/content/repositories/releases",
  Resolver.bintrayRepo("websudos", "oss-releases")
)

libraryDependencies ++= {
  val akkaVersion = "2.4.7"
  val sprayVersion = "1.3.3"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "io.spray" %% "spray-can" % sprayVersion,
    "io.spray" %% "spray-routing-shapeless2" % sprayVersion,
    "io.spray" %% "spray-httpx" % sprayVersion,
    "io.spray" %% "spray-json" % "1.3.2",
    "com.websudos" %% "phantom-dsl" % "1.27.0",

    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
    "io.spray" %%  "spray-testkit" % sprayVersion % "test",
    "org.specs2" %% "specs2-core" % "2.3.11" % "test"
  )
}

Revolver.settings