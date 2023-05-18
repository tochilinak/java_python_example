plugins {
    kotlin("jvm") version "1.8.20"
    java
    application
    c
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

tasks.run.configure {
    environment("LD_PRELOAD" to "/home/tochilinak/Documents/projects/utbot/dist_python/my_dist/libpython3.11.so.1.0")
}

val config = tasks.register("CPythonBuildConfiguration") {

}

val cpython: TaskProvider<Exec> = tasks.register<Exec>("CPythonBuild") {
    commandLine("echo", "\"!!!!!!!\"")
}

tasks.test {
    useJUnitPlatform()
}

tasks.classes {
    dependsOn(":cpythonadapter:linkDebug")
    dependsOn(cpython)
}


kotlin {
    jvmToolchain(8)
}