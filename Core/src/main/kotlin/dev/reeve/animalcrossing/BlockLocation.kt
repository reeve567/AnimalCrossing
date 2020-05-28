package dev.reeve.animalcrossing

import org.bukkit.World
import org.bukkit.block.Block

class BlockLocation(var x: Int, var y: Int, var z: Int) {
    fun getBlock(world: World): Block {
        return world.getBlockAt(x, y, z)
    }

    fun getRelative(x: Int, y: Int, z: Int): BlockLocation {
        return BlockLocation(this.x + x, this.y + y, this.z + z)
    }
}