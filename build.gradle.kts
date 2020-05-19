val logback_version: String by project
val kotlin_version: String by project
val spek_version: String by project
val jackson_version: String by project
val kluent_version: String by project
val mockk_version: String by project
val koin_version : String by project

buildscript {
    repositories {
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.41")
    }
}

plugins {
    java
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

