import sbt.*
import uk.gov.hmrc.versioning.SbtGitVersioning.autoImport.majorVersion
import org.scalafmt.sbt.ScalafmtPlugin
import uk.gov.hmrc.DefaultBuildSettings

val appName = "individuals-partner-income-api"

ThisBuild / majorVersion := 0
ThisBuild / scalaVersion := "3.5.2"
ThisBuild / scalacOptions ++= Seq("-Werror", "-Wconf:msg=Flag.*repeatedly:s")


lazy val microservice = Project(appName, file("."))
  .enablePlugins(play.sbt.PlayScala, SbtDistributablesPlugin)
  .disablePlugins(JUnitXmlReportPlugin) // Required to prevent https://github.com/scalatest/scalatest/issues/1427
  .settings(
    libraryDependencies ++= AppDependencies.compile ++ AppDependencies.test,
    scalafmtOnCompile               := true,
    scalacOptions ++= List(
      "-Wconf:src=routes/.*:s",
      "-feature"
    )
  )
  .settings(
    Compile / unmanagedResourceDirectories += baseDirectory.value / "resources",
    Compile / unmanagedClasspath += baseDirectory.value / "resources"
  )
  .settings(CodeCoverageSettings.settings)
  .settings(PlayKeys.playDefaultPort := 7766)

lazy val it = project
  .enablePlugins(PlayScala)
  .dependsOn(microservice % "test->test")
  .settings(DefaultBuildSettings.itSettings())
  .settings(
    Test / fork := true,
    Test / javaOptions += "-Dlogger.resource=logback-test.xml")
  .settings(libraryDependencies ++= AppDependencies.itDependencies)
