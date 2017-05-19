import ByteConversions._

organization := "com.typesafe.conductr"
name := "cassandra"
version := "3.10"

licenses := Seq("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0"))

libraryDependencies += "org.apache.cassandra" % "cassandra-all" % "3.10"

// Bundle configuration

BundleKeys.nrOfCpus := 1.1
BundleKeys.memory := 1.GiB
BundleKeys.diskSpace := 100.MB
BundleKeys.roles := Set("cassandra")

BundleKeys.endpoints := Map(
  "cas_native" ->  Endpoint("tcp", 0, serviceName = "cas_native", RequestAcl(Tcp(9042))),
  "cas_rpc" ->     Endpoint("tcp"),
  "cas_storage" -> Endpoint("tcp", 7000)
)

BundleKeys.executableScriptPath in Bundle := (file((normalizedName in Bundle).value) / "bin" / "bootstrap").getPath
BundleKeys.checks := Seq(
  uri("$CAS_STORAGE_HOST?retry-count=10&retry-delay=3")
)

BundleKeys.configurationName := "cassandra-prod"

javaOptions in Bundle := Seq.empty

// Bundle publishing configuration

def bintrayBundleSettings = Seq(
  bintrayVcsUrl := Some("https://github.com/typesafehub/conductr-cassandra"),
  bintrayOrganization := Some("typesafe")
)

inConfig(Bundle)(bintrayBundleSettings)
inConfig(BundleConfiguration)(bintrayBundleSettings)

lazy val root = project.in(file(".")).enablePlugins(JavaServerAppPackaging)
