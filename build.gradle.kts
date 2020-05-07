group = "dev.reeve"
version = "1.0"

plugins {
    kotlin("jvm") version "1.3.72"
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

subprojects {
    group = "dev.reeve"
    version = "1.0"
    repositories {
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://oss.sonatype.org/content/repositories/central")
        mavenLocal()
    }
}

allprojects {
    repositories {
        mavenCentral()
        jcenter()
    }
}


tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }

    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }

    tasks {
        shadowJar {
            minimize()
            configurations = mutableListOf(project.configurations.implementation.get())
        }
    }
}