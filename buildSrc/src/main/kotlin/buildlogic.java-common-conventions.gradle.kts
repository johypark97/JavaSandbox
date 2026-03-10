import org.gradle.accessors.dm.LibrariesForLibs

val libs = the<LibrariesForLibs>()

// https://javadoc.io/doc/org.mockito/mockito-core/latest/org.mockito/org/mockito/Mockito.html#0.3
val mockitoAgent = configurations.create("mockitoAgent")

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
    annotationProcessor(libs.dagger.compiler)
    implementation(libs.dagger)
    implementation(libs.gson)
    implementation(libs.guava)
    implementation(libs.rxjava)

    // -------- Logging --------
    // implementation(libs.log4j.api)
    // implementation(libs.log4j.core)

    implementation(libs.log4j.slf4j2.impl)
    implementation(libs.slf4j.api)

    // -------- Test libraries --------
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.mockito.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)

    mockitoAgent(libs.mockito.core) { isTransitive = false }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
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
    // Suppress the following warnings, but may degrade app startup performance.
    // `OpenJDK 64-Bit Server VM warning: Sharing is only supported for boot loader classes because bootstrap classpath has been appended`
    jvmArgs("-Xshare:off")

    // Add the Mockito-core as a Java agent.
    // Note: `jvmArgs.add()` is not working.
    jvmArgs("-javaagent:${mockitoAgent.asPath}")

    useJUnitPlatform()
}
