import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

val appName = "JavaSandbox"
val buildBasename = "jsandbox"
val buildVersion = Version.makeVersionString()

plugins {
    id("project.java-application-conventions")
}

dependencies {}

application {
    mainClass.set("javasandbox.Main")
    mainModule.set("javasandbox")

    applicationName = appName
    executableDir = ""

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
    dependsOn(tasks.named("processResources_buildProperties"))
}

tasks.jar {
    manifest {
        attributes["Implementation-Version"] = buildVersion
        attributes["Main-Class"] = application.mainClass.get()
    }

    archiveBaseName.set(buildBasename)
    archiveVersion.set(buildVersion)
}
