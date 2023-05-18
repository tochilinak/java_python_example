plugins {
    kotlin("jvm") version "1.8.20"
    java
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    // https://mvnrepository.com/artifact/net.java.dev.jna/jna
    // implementation("net.java.dev.jna:jna:5.13.0")
}

application {
    mainClass.set("MainKt")
    applicationDefaultJvmArgs = listOf("-Djava.library.path=${System.getProperty("java.library.path")}:${project(":cpythonadapter").buildDir}/lib/main/debug")
}

tasks.test {
    useJUnitPlatform()
}

tasks.classes {
    dependsOn(":cpythonadapter:linkDebug")
}

val cpythonPath = "${rootProject.rootDir.path}/cpython"
val cpythonBuildPath = "${rootProject.buildDir.path}/cpython_build"

val cpythonClean = tasks.register<Exec>("cleanCPython") {
    workingDir = File(cpythonPath)
    commandLine("make", "distclean")
}

tasks.clean {
    dependsOn(cpythonClean)
}

kotlin {
    jvmToolchain(8)
}

tasks.run.configure {
    environment("LD_LIBRARY_PATH" to "$cpythonBuildPath/lib")
    environment("LD_PRELOAD" to "$cpythonBuildPath/lib/libpython3.so")
}