package dev.reeve.animalcrossing.island.trees

import org.bukkit.Location

class IslandTrees : ArrayList<IslandTree>() {
	
	fun shakeTree(location: Location) {
		forEach { tree ->
			if (tree.isTree(location)) {
				println("tree")
				tree.shakeTree()
				return@forEach
			}
		}
	}
	
	fun generateFruits() {
		forEach {
			it.generateFruit()
		}
	}
	
	fun generateSticks() {
		forEach {
			it.generateSticks()
		}
	}
}