import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

// Sets project version.
version = Version.makeVersionString()

plugins {
    id("buildlogic.java-application-conventions")
}

dependencies {}

application {
    mainClass = "javasandbox.App"
    mainModule = "javasandbox"

    // Sets the executable name of the startScripts task. If not set, the project name is used.
    applicationName = "JavaSandbox"

    // Sets the location of the executable. Setting an empty string moves the executable to the root
    // directory.
    executableDir = ""

    applicationDefaultJvmArgs += "-Dlog.level=ALL"

    // Includes the '$projectDir/data' directory in the archive file of the distribution task.
    // applicationDistribution.into("data") {
    //     from("data")
    // }
}

// Another way to include the '$projectDir/data' directory.
// distributions.main.get().contents.into("data") {
//     from("data")
// }

tasks.jar {
    manifest {
        attributes["Implementation-Version"] = version
        attributes["Main-Class"] = application.mainClass
    }

    // Sets the name of the jar file. If not set, the project name is used.
    archiveBaseName = "javasandbox"

    // Sets the version of the jar file. If not set, the project version is used.
    // archiveVersion = version.toString()
}

tasks.processResources {
    dependsOn("buildProperties")
}

tasks.register<WriteProperties>("buildProperties") {
    description = "Creates a properties file containing the build information."

    destinationFile = File(sourceSets.main.get().output.resourcesDir, "build.properties")
    property("build.date", ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS))
    property("build.version", version)
}
