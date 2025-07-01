import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

// Sets the project version.
version = findProperty("version") as String

plugins {
    id("buildlogic.java-application-conventions")

    alias(libs.plugins.jlink.plugin)
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

    applicationDefaultJvmArgs += "-Ddebug=true"
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

jlink {
    val optionList = mutableListOf("--no-header-files", "--no-man-pages")
    // optionList += "--strip-debug"

    options = optionList.toList()

    // slf4j - log4j2
    addExtraDependencies("log4j-api")
    addExtraDependencies("log4j-core")
    addExtraDependencies("log4j-slf4j2-impl")
    addExtraDependencies("slf4j-api")

    mergedModule {
        additive = true

        // slf4j - log4j2
        run {
            requires("java.naming") // Fixes the JNDI lookup class warning.

            requires("org.apache.logging.log4j.core")
            requires("org.apache.logging.log4j.slf4j2.impl")
        }
    }

    launcher {
        // Sets the executable name of the jlink task. If not set, the project name is used.
        name = application.applicationName
    }

    jpackage {
        // Sets the executable and directory name of the jpackageImage task. If not set, the name
        // set in the launcher block or the project name is used.
        // imageName = application.applicationName
    }
}

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
