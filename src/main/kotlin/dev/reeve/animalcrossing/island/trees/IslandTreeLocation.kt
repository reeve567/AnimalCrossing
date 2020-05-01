package dev.reeve.animalcrossing.island.trees

import org.bukkit.World
import org.bukkit.block.Block

class IslandTreeLocation(var x: Int, var y: Int, var z: Int, private var world: World) {
	val block: Block
		get() : Block {
			return world.getBlockAt(x, y, z)
		}
	
	override fun toString(): String {
		return "IslandTreeLocation(x=$x, y=$y, z=$z, world=$world)"
	}
	
}