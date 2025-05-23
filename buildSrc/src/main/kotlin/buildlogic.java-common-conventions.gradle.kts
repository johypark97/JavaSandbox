import org.gradle.accessors.dm.LibrariesForLibs

val libs = the<LibrariesForLibs>()
val mockitoAgent: Configuration = configurations.create("mockitoAgent")

plugins {
    java

    // JavaFX
    id("org.openjfx.javafxplugin")

    // Static analysis tools
    pmd
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.guava)

    // -------- Logging --------
    // implementation(libs.log4j.api)
    // implementation(libs.log4j.core)

    implementation(libs.log4j.slf4j2.impl)
    implementation(libs.slf4j.api)

    // -------- Test libraries --------
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.mockito.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)

    @Suppress("UnstableApiUsage") mockitoAgent(libs.mockito.core) { isTransitive = false }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

javafx {
    // You may need to manually download module source files to use IntelliSense.
    // https://mvnrepository.com/artifact/org.openjfx

    // Module list:
    // - javafx.base
    // - javafx.controls
    // - javafx.fxml
    // - javafx.graphics
    // - javafx.media
    // - javafx.swing
    // - javafx.swt
    // - javafx.web

    modules = listOf()
    version = libs.versions.javafx.version.get()
}

pmd {
    isIgnoreFailures = false
    toolVersion = libs.versions.pmd.get()

    ruleSetFiles = files("$rootDir/buildSrc/config/pmd/rules.xml")
    ruleSets = emptyList()
}

tasks.named<Test>("test") {
    jvmArgs("-javaagent:${mockitoAgent.asPath}")
    useJUnitPlatform()
}
