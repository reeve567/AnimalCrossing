plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
}

repositories {
    maven("http://nexus.okkero.com/repository/maven-releases/")
    maven("https://jitpack.io")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.okkero.skedule:skedule:1.2.6")
    compileOnly(project(":Generator"))
    compileOnly(project(":Utility"))
    compileOnly("org.spigotmc:spigot-api:1.15.2-R0.1-SNAPSHOT")
    compileOnly("org.bukkit:craftbukkit:1.15.2-R0.1-SNAPSHOT")
    compile("junit:junit:4.13")
}
