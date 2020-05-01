package dev.reeve.animalcrossing.generation

import org.bukkit.Chunk
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.generator.BlockPopulator
import org.bukkit.util.noise.SimplexOctaveGenerator
import java.util.*
import kotlin.math.abs
import kotlin.math.min

const val riverDepth = waterLevel - 2
const val riverFrequency = 0.0001
const val riverAmplitude = .01
val riverGenerator = SimplexOctaveGenerator(Random(System.nanoTime()), 1).apply {
	setScale(0.0015)
}

class RiverPopulator : BlockPopulator() {
	private var chunks = 0
	
	fun World.getHighest(x: Int, z: Int): Int {
		for (y in 120 downTo waterLevel - riverDepth - 1) {
			val block = getBlockAt(x, y, z)
			if (block.type != Material.AIR && !block.isLiquid) {
				return y
			}
		}
		return 0
	}
	
	override fun populate(world: World, random: Random, source: Chunk) {
		println("chunks: ${++chunks} [${source.x},${source.z}]")
		for (x in 0 until 16) {
			for (z in 0 until 16) {
				val fullX = source.x * 16 + x
				val fullZ = source.z * 16 + z
				val highest = world.getHighest(fullX, fullZ)
				
				if (highest > riverDepth) {
					val difference = highest - riverDepth
					val multiplier = abs(riverGenerator.noise(
							fullX.toDouble(),
							fullZ.toDouble(),
							riverFrequency,
							riverAmplitude,
							true))
					
					if (multiplier > 0.5) {
						val before = difference
						val mult = 1 - min(multiplier, 1.0)
						val toRemove = (mult * difference).toInt()
						println("highest: $highest difference: $difference before: $before toRemove: $toRemove mult: $mult riverDepth: $riverDepth")
						
						/*for (i in highest downTo difference + riverDepth) {
							//println(i)
							if (i <= waterLevel) {
								world.getBlockAt(fullX, i, fullZ).setType(Material.WATER, false)
							} else {
								world.getBlockAt(fullX, i, fullZ).setType(Material.AIR, false)
							}
						}*/
						
						/*for (i in 0 until toRemove) {
							if (highest - i <= waterLevel) {
								world.getBlockAt(fullX, highest - i, fullZ).setType(Material.WATER, false)
							} else
								world.getBlockAt(fullX, highest - i, fullZ).setType(Material.AIR, false)
						}*/
						
						/*val y = (difference - 1) + riverDepth
						val material = WorldGenerator.getMaterialTop(y)
						world.getBlockAt(fullX, y, fullZ).setType(material, false)
						world.getBlockAt(fullX, y - 1, z).setType(WorldGenerator.getMaterialSecond(material), false)*/
					}
				}
			}
		}
	}
	
	
}