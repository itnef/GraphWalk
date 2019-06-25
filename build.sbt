resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
resolvers += "akka" at "http://repo.akka.io/snapshots"

name := "GraphExperiments"

// defaults to src/main/resources:
// resourceDirectory in Compile := baseDirectory.value / "resources"
// resourceDirectory in Test    := baseDirectory.value / "resources"

libraryDependencies ++= Seq(
  "commons-io" % "commons-io" % "2.5",

  "com.typesafe.akka" %% "akka-actor"   % "2.5.23",
  "com.typesafe.akka" %% "akka-slf4j"   % "2.5.23",
  "com.typesafe.akka" %% "akka-remote"  % "2.5.23",
  "com.typesafe.akka" %% "akka-agent"   % "2.5.23",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.23" % "test"
)
