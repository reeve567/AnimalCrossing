package dev.reeve.animalcrossing.island.trees

import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.block.Block

class IslandTreeLocation(var x: Int, var y: Int, var z: Int, private var world: String) {
	val block: Block
		get() : Block {
			return Bukkit.getWorld(world)!!.getBlockAt(x, y, z)
		}
	
	override fun toString(): String {
		return "IslandTreeLocation(x=$x, y=$y, z=$z, world=$world)"
	}
	
}