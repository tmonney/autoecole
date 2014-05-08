scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

resolvers += "Typesafe private repo" at "http://private-repo.typesafe.com/typesafe/"

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.4.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "0.7.0-RC3")

addSbtPlugin("com.typesafe.sbt" % "sbt-less" % "1.0.0-RC1")

addSbtPlugin("com.typesafe.sbt" % "sbt-jshint" % "1.0.0-RC1")

addSbtPlugin("com.typesafe.sbt" % "sbt-rjs" % "1.0.0-RC1")

addSbtPlugin("com.typesafe.sbt" % "sbt-digest" % "1.0.0-RC1")

