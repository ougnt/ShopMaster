name := "MemberMaster"

version := "1.0"

val sVersion = "2.11.6"

scalaVersion := sVersion

//libraryDependencies += "io.reactivex" %% "rxscala" % "0.26.2"
libraryDependencies += "com.scalarx" % "scalarx_2.10" % "0.1"
libraryDependencies += "org.scalafx" % "scalafx_2.11" % "8.0.92-R10"
libraryDependencies += "joda-time" % "joda-time" % "2.9.4"
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.12"
//
//libraryDependencies ++= Seq(
//    jdbc,
//    anorm,
//    cache,
//    ws
//)

unmanagedJars in Compile += Attributed.blank(file(System.getenv("JAVA_HOME") + "/jre/lib/ext/jfxrt.jar"))
