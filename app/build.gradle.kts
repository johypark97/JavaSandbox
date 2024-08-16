import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

val buildVersion = Version.makeVersionString()

plugins {
    id("buildlogic.java-application-conventions")
}

dependencies {}

application {
    mainClass = "javasandbox.App"
    mainModule = "javasandbox"

    applicationName = "JavaSandbox"
    executableDir = ""

    applicationDefaultJvmArgs += "-Dlog.level=ALL"

    // Include '$projectDir/data' directory in archive files of the distribution task.
    // applicationDistribution.into("data") {
    //     from("data")
    // }
}

// Another way to include the '$projectDir/data' directory.
// distributions.main.get().contents.into("data") {
//     from("data")
// }

tasks.register<WriteProperties>("processResources_buildProperties") {
    description = "Creates a properties file containing the build information."

    destinationFile = File(sourceSets.main.get().output.resourcesDir, "build.properties")
    property("build.date", ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS))
    property("build.version", buildVersion)
}

tasks.processResources {
    dependsOn("processResources_buildProperties")
}

tasks.jar {
    manifest {
        attributes["Implementation-Version"] = buildVersion
        attributes["Main-Class"] = application.mainClass.get()
    }

    archiveBaseName = "jsandbox"
    archiveVersion = buildVersion
}
