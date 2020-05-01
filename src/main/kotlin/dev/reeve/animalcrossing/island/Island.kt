package dev.reeve.animalcrossing.island

import dev.reeve.animalcrossing.generation.islandSize
import dev.reeve.animalcrossing.island.trees.FruitType
import dev.reeve.animalcrossing.island.trees.IslandTree
import dev.reeve.animalcrossing.island.trees.IslandTreeLocation
import dev.reeve.animalcrossing.island.trees.IslandTrees
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import java.util.*

class Island(
		val location: IslandLocation,
		val owner: UUID,
		private var nativeFruitType: FruitType? = null,
		val islandId: UUID = UUID.randomUUID()
) {
	val trees = IslandTrees()
	private val world = Bukkit.getWorld("world")!!
	
	init {
		println("[${location.x}, ${location.z}]")
		if (nativeFruitType == null) {
			nativeFruitType = FruitType.values().random()
			findTrees()
			trees.generateFruits()
			trees.generateSticks()
		}
	}
	
	private fun findTrees() {
		for (chunkX in 0 until islandSize) {
			for (chunkZ in 0 until islandSize) {
				val chunk = Location(world, (location.x + chunkX) * 16.0, 1.0, (location.z + chunkZ) * 16.0).chunk
				val highest = world.getHighestBlockYAt(chunk.getBlock(0, 1, 0).location) - 2
				chunk@ for (y in 0 until 10) {
					for (x in 0 until 16) {
						for (z in 0 until 16) {
							val block = chunk.getBlock(x, y + highest, z)
							if (block.type == Material.OAK_LOG || block.type == Material.OAK_LEAVES) {
								trees.add(IslandTree(
										IslandTreeLocation((location.x + chunkX) * 16 + x, highest + y, (location.z + chunkZ) * 16 + z, world),
										nativeFruitType!!
								))
								break@chunk
							}
						}
					}
				}
			}
		}
	}
	
	fun isIsland(x: Int, z: Int): Boolean {
		return location.x == (x / islandSize) * islandSize && location.z == (z / islandSize) * islandSize
	}
}