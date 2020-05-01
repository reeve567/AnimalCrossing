package dev.reeve.animalcrossing.generation

import dev.reeve.animalcrossing.extensions.up
import org.bukkit.Chunk
import org.bukkit.TreeType
import org.bukkit.World
import org.bukkit.generator.BlockPopulator
import java.util.*

class TreePopulator : BlockPopulator() {
	private val treesPerChunk = 1
	private val maxTries = 5
	
	override fun populate(world: World, random: Random, source: Chunk) {
		for (i in 0 until treesPerChunk) {
			var tries = 0
			while (tries < maxTries) {
				tries++
				val block = world.getHighestBlockAt(source.x * 16 + Random().nextInt(10) + 3, source.z * 16 + Random().nextInt(10) + 3)
				if (!block.isLiquid) {
					world.generateTree(block.location.up(), TreeType.TREE)
					break
				}
			}
		}
	}
	
}