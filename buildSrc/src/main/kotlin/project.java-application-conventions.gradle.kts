plugins {
    id("project.java-common-conventions")

    application
}

application {
    applicationDefaultJvmArgs = listOf("-Dfile.encoding=UTF-8")
}
