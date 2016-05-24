name := "BasicFinagle"

version := "1.0"

scalaVersion := "2.11.8"

///////////////////////////////////////////////////////////////////////////////
//     DEPENDENCIES

libraryDependencies ++= Seq(
  "com.twitter" %% "finagle-http" % "6.35.0",
  "com.twitter" % "twitter-server_2.11" % "1.20.0",
  "com.twitter.common"   %  "metrics"           %   "0.0.37"
)


//test
libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "org.apache.curator" % "curator-test" % "2.9.1" % "test",
  "org.mockito" % "mockito-core" % "1.10.19" % "test",
  "org.apache.httpcomponents" % "httpclient" % "4.5.2"
)

///////////////////////////////////////////////////////////////////////////////
//     TEST OPTIONS

//TODO move all the config to the project format and add proper integration test scope
//      see http://www.scala-sbt.org/0.13/docs/Testing.html

parallelExecution in Test := true

testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-u", "target/test-reports-junit")

///////////////////////////////////////////////////////////////////////////////
//     COMPILE OPTIONS

import com.typesafe.sbt.SbtNativePackager.{packageArchetype, _}
import com.typesafe.sbt.SbtNativePackager.autoImport._
import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform.autoImport._
packageArchetype.java_application

scalacOptions ++= Seq(
  "-deprecation",
  "-unchecked",
  "-feature",
  "-optimize",
  "-encoding", "utf8",
  "-language:postfixOps",
  "-language:reflectiveCalls",
  "-language:implicitConversions",
  "-Yinline-warnings")

evictionWarningOptions in update := EvictionWarningOptions.default
  .withWarnTransitiveEvictions(false)
  .withWarnDirectEvictions(false)
  .withWarnScalaVersionEviction(false)

ivyScala := ivyScala.value map {
  _.copy(overrideScalaVersion = true)
}

javacOptions ++= Seq(
  //"-Xlint:unchecked",
  //"-Xlint:deprecation",
  "-Xlint",
  "-source", "1.8",
  "-target", "1.8"

)

fork in run := true






///////////////////////////////////////////////////////////////////////////////
//     RESOLVERS

resolvers ++= Seq(
 "Twitter Maven repo" at "http://maven.twttr.com/",
  "Maven Central" at "http://repo1.maven.org/maven2/"
)

externalResolvers <<= resolvers map { rs => Resolver.withDefaultResolvers(rs, false) }





///////////////////////////////////////////////////////////////////////////////
//     MAVEN



///////////////////////////////////////////////////////////////////////////////
// CODE FORMATTING

import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform

import scalariform.lexer.NewlineInferencer

SbtScalariform.defaultScalariformSettings

scalariformPreferences := scalariformPreferences.value
  .setPreference(AlignParameters, true)
  .setPreference(AlignArguments, true)
  .setPreference(IndentPackageBlocks, true)
  .setPreference(CompactStringConcatenation, false)
  .setPreference(DoubleIndentClassDeclaration, true)
  .setPreference(IndentSpaces, 2)
  .setPreference(PreserveSpaceBeforeArguments, false)
  .setPreference(RewriteArrowSymbols, false)
  .setPreference(SpaceBeforeColon, false)
  .setPreference(SpaceInsideBrackets, false)
  .setPreference(SpaceInsideParentheses, false)
  .setPreference(SpacesAroundMultiImports, false)
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(AlignSingleLineCaseStatements.MaxArrowIndent, 10)
  .setPreference(PlaceScaladocAsterisksBeneathSecondAsterisk, true)

    