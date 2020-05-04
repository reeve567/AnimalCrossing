package dev.reeve.animalcrossing.island.trees

import dev.reeve.animalcrossing.TickToTime
import org.bukkit.Location
import org.bukkit.Material
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class IslandTree(
		private val treeBase: IslandTreeLocation,
		private val fruitType: FruitType,
		private val lastGeneratedFruit: Long = 0,
		private val lastGeneratedSticks: Long = 0,
		private var sticksDropped: Int = 0
) {
	private val maxFruit = 3
	private val maxSticks = 16
	private var currentFruit = ArrayList<Location>()
	
	init {
		loadFruit()
	}
	
	private fun getBlocks(material: Material): ArrayList<Location> {
		val blocks = ArrayList<Location>()
		var notDone = true
		var y = 0
		while (notDone) {
			y++
			if (treeBase.block.getRelative(0, y, 0).type == Material.AIR) {
				notDone = false
			}
			for (x in -2..2) {
				for (z in -2..2) {
					val block = treeBase.block.getRelative(x, y, z)
					if (block.type == material) {
						blocks.add(block.location)
					}
				}
			}
		}
		return blocks
	}
	
	private fun loadFruit() {
		currentFruit.addAll(getBlocks(fruitType.material))
	}
	
	fun generateFruit() {
		if (Date().time - lastGeneratedFruit >= TickToTime.getTicks(3, 0, 0, 0)) {
			if (currentFruit.size < maxFruit) {
				val leaves = getBlocks(Material.OAK_LEAVES)
				
				while (currentFruit.size < maxFruit) {
					val random = leaves.random()
					random.block.type = fruitType.material
					currentFruit.add(random)
					leaves.remove(random)
				}
			}
		}
	}
	
	fun generateSticks() {
		if (Date().time - lastGeneratedSticks >= TickToTime.getTicks(1, 0, 0, 0)) {
			sticksDropped = 0
		}
	}
	
	private fun replaceFruit(index: Int) {
		currentFruit[index].block.type = Material.OAK_LEAVES
		currentFruit.removeAt(index)
	}
	
	fun shakeTree() {
		val random = Random.nextInt(0,2)
		when (random) {
			0 -> {
				//fruit
				
			}
			1 -> {
				//stick
			}
		}
	}
	
	fun isTree(location: Location): Boolean {
		return location.blockX == treeBase.x && location.blockZ == treeBase.z
	}
}