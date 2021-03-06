import org.gradle.jvm.tasks.Jar
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

val logback_version: String by project
val kotlin_version: String by project
val spek_version: String by project
val jackson_version: String by project
val kluent_version: String by project
val mockk_version: String by project
val koin_version : String by project
val mongo_version : String by project
val ktor_version : String by project

buildscript {
    repositories {
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.41")
        classpath("com.github.jengelman.gradle.plugins:shadow:5.1.0")
    }
}

plugins {
    java
    application
    id("com.github.johnrengelman.shadow") version "4.0.4"
}

allprojects {
    group = "com.soumyajit"
    version = "0.0.1"

    apply(plugin = "org.jetbrains.kotlin.jvm")

    repositories {
        jcenter()
        mavenLocal()
        maven { url = uri("https://kotlin.bintray.com/ktor") }
    }

    dependencies {
        implementation("ch.qos.logback:logback-classic:$logback_version")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jackson_version")
        implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jackson_version")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
        implementation("org.koin:koin-ktor:$koin_version")
        implementation ("org.mongodb:mongodb-driver:$mongo_version")
        implementation("io.ktor:ktor-server-cio:$ktor_version")
        implementation("io.ktor:ktor-server-netty:$ktor_version")

        testImplementation("org.amshove.kluent:kluent:$kluent_version")
        testImplementation("org.spekframework.spek2:spek-dsl-jvm:$spek_version")
        testImplementation("io.mockk:mockk:$mockk_version")

        testRuntime("org.spekframework.spek2:spek-runner-junit5:$spek_version")
    }

    tasks.withType<Test> {
        useJUnitPlatform {
            includeEngines("spek2")
        }
    }
}

sourceSets.getByName("main") {
    java.srcDir("src/main/kotlin")
    resources.srcDir("src/main/resource")
}

subprojects {
    version = "1.0"
}

project(":dataccess-service") {
    dependencies {
        implementation(project(":todolist-shared"))
        implementation(project(":repository"))
    }
}
project(":repository") {
    dependencies {
        implementation(project(":todolist-shared"))
    }
}
project(":todolist-shared") {

}
project(":todolist-restapi") {
    dependencies {
        implementation(project(":dataccess-service"))
        implementation(project(":todolist-shared"))
        implementation(project(":repository"))
    }
}

application {
    applicationName = "Ktor-Sample"
    mainClassName = "io.ktor.server.netty.EngineMain"
}

tasks {

    val fatJar = task("createJar", type = Jar::class) {
        val version = "1.0-SNAPSHOT"
        archiveName = "${application.applicationName}-$version.jar"
        manifest {
            attributes["Version"] = version
            attributes["Main-Class"] = application.mainClassName
        }
        from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
        destinationDirectory.set(project.rootDir)
    }

    /*val shadowJar = named<ShadowJar>("shadowJar") {
        archiveBaseName.set(application.applicationName)
        mergeServiceFiles()
        manifest {
            attributes(mapOf("Main-Class" to application.mainClassName))
        }
        destinationDirectory.set(project.rootDir)
    }*/

    /*"build" {
        dependsOn(fatJar)
    }*/

    val stage by registering(DefaultTask::class)
    stage {
        dependsOn("installDist")
    }
}

