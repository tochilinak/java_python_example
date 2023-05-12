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
    //environment("LD_LIBRARY_PATH", "path/to/lib")
    applicationDefaultJvmArgs = listOf("-Djava.library.path=${System.getProperty("java.library.path")}:${project(":cpythonadapter").buildDir}/lib/main/debug")
}

tasks.test {
    useJUnitPlatform()
}

tasks.classes {
    dependsOn(":cpythonadapter:linkDebug")
}


kotlin {
    jvmToolchain(8)
}