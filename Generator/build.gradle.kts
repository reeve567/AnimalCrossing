plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":Utility"))
    compileOnly("org.spigotmc:spigot-api:1.15.2-R0.1-SNAPSHOT")
    compileOnly("org.bukkit:craftbukkit:1.15.2-R0.1-SNAPSHOT")
}