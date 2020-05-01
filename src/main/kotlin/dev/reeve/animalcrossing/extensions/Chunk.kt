package dev.reeve.animalcrossing.extensions

import org.bukkit.Chunk
import org.bukkit.Material

fun Chunk.setBlock(x: Int, y: Int, z: Int, material: Material) {
	world.getBlockAt(this.x * 16 + x, y, this.z * 16 + z).type = material
}