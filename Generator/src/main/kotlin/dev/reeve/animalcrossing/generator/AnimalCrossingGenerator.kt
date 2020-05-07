package dev.reeve.animalcrossing.generator

import org.bukkit.generator.ChunkGenerator
import org.bukkit.plugin.java.JavaPlugin

class AnimalCrossingGenerator : JavaPlugin() {
    override fun getDefaultWorldGenerator(worldName: String, id: String?): ChunkGenerator? {
        return WorldGenerator(true)
    }
}