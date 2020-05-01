package dev.reeve.animalcrossing.generation

import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.Biome
import org.bukkit.generator.BlockPopulator
import org.bukkit.generator.ChunkGenerator
import org.bukkit.util.noise.SimplexOctaveGenerator
import java.util.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

const val worldHeight = 45
const val islandSize = 29
const val waterLevel = 66

class WorldGenerator(private val useWorldSeed: Boolean = true) : ChunkGenerator() {
	
	companion object {
		fun getMaterialTop(y: Int): Material {
			return when {
				y == waterLevel -> Material.SAND
				y > waterLevel -> {
					Material.GRASS_BLOCK
				}
				y < waterLevel - 4 -> {
					Material.GRAVEL
				}
				else -> Material.DIRT
			}
		}
		
		fun getMaterialSecond(top: Material): Material {
			return when (top) {
				Material.GRAVEL -> top
				Material.SAND -> top
				else -> Material.DIRT
			}
		}
	}
	
	private fun distance(x: Double, z: Double): Int {
		return sqrt(x * x + z * z).toInt()
	}
	
	override fun generateChunkData(
			world: World,
			random: Random,
			chunkX: Int,
			chunkZ: Int,
			biome: BiomeGrid
	): ChunkData {
		val chunkData = createChunkData(world)
		
		val seed = if (useWorldSeed) world.seed else System.currentTimeMillis()
		
		val generator = SimplexOctaveGenerator(Random(seed), 8)
		generator.setScale(0.005)
		
		val smallerGenerator = SimplexOctaveGenerator(Random(seed), 8)
		smallerGenerator.setScale(0.02)
		
		val riverGenerator = SimplexOctaveGenerator(Random(seed * 2), 4)
		riverGenerator.setScale(0.0025)
		
		if (chunkX < 0 || chunkZ < 0) {
			return chunkData
		}
		
		val islandCornerChunkX = (chunkX / islandSize) * islandSize
		val islandCornerChunkZ = (chunkZ / islandSize) * islandSize
		
		val islandCenterX = (islandSize / 2.0) + islandCornerChunkX
		val islandCenterZ = (islandSize / 2.0) + islandCornerChunkZ
		
		val radius = islandSize * 8
		
		//println("[$chunkX, $chunkZ] islandSize: $islandSize islandCorner: [$islandCornerChunkX, $islandCornerChunkZ] islandCenter: [$islandCenterX, $islandCenterZ]")
		
		for (x in 0 until 16) {
			for (z in 0 until 16) {
				for (y in 0 until 150) {
					biome.setBiome(x, y, z, Biome.FOREST)
				}
				
				val distanceCenterX = abs(islandCenterX * 16 - (chunkX * 16 + x))
				val distanceCenterZ = abs(islandCenterZ * 16 - (chunkZ * 16 + z))
				
				val distance = distance(distanceCenterX, distanceCenterZ)
				
				// circle
				val i = distance / radius.toDouble()
				
				//square
				val j = max(distanceCenterX, distanceCenterZ) / radius.toDouble()
				
				var value = if (j <= 0.3) {
					(-0.2 * j) + 1
				} else {
					((1 / (j + .5)) - 0.31)
				}
				
				if (j >= .95) value = .1
				
				//println("distance: $distance radius: $radius distanceCenter: [$distanceCenterX,$distanceCenterZ] value: $value")
				
				if (value > 1) {
					println("value bigger than 1: $value")
				} else if (value < 0) {
					println("value smaller than 0: $value")
				}
				
				
				var currentHeight = (value * 30)
				currentHeight +=
						generator.noise(
								chunkX * 16.0 + x,
								chunkZ * 16.0 + z,
								0.01,
								0.003,
								true
						) * 3
				
				fun getHeight(): Double {
					val heightValue =
							min(
									smallerGenerator.noise(
											chunkX * 16.0 + x,
											chunkZ * 16.0 + z,
											0.001,
											0.001,
											true
									) * value * 3, 0.0
							)
					return heightValue
				}
				
				fun indentsGenerator(): Double {
					return max(
							riverGenerator.noise(
									chunkX * 16.0 + x,
									chunkZ * 16.0 + z,
									0.05,
									0.1,
									true
							) * 5 * value, 0.0
					)
				}
				
				currentHeight += getHeight()
				currentHeight -= indentsGenerator()
				currentHeight += worldHeight
				/*if (currentHeight > waterLevel - riverDepth) {
					var difference = currentHeight - (waterLevel - riverDepth)
					val before = difference
					difference *= (1 - riverGenerator.noise(
							chunkX * 16.0 + x,
							chunkZ * 16.0 + z,
							0.00001,
							0.01,
							true
					))
					currentHeight = (waterLevel - riverDepth) + difference
				}*/
				
				currentHeight -= abs(riverGenerator.noise(
						chunkX * 16.0 + x,
						chunkZ * 16.0 + z,
						riverFrequency,
						riverAmplitude,
						true
				) * 6)
				
				if (distanceCenterX == 0.0 && distanceCenterZ == 0.0) {
					chunkData.setBlock(x, currentHeight.toInt() + 1, z, Material.RED_WOOL)
				}
				
				val topBlock = getMaterialTop(currentHeight.toInt())
				
				val secondBlock = getMaterialSecond(topBlock)
				
				chunkData.setBlock(x, currentHeight.toInt(), z, topBlock)
				chunkData.setBlock(x, currentHeight.toInt() - 1, z, secondBlock)
				for (y in 1 until currentHeight.toInt() - 1) {
					chunkData.setBlock(x, y, z, Material.STONE)
				}
				
				for (y in currentHeight.toInt() + 1..waterLevel) {
					chunkData.setBlock(x, y, z, Material.WATER)
				}
				chunkData.setBlock(x, 0, z, Material.BEDROCK)
			}
		}
		return chunkData
	}
	
	override fun getDefaultPopulators(world: World): MutableList<BlockPopulator> {
		return mutableListOf(TreePopulator())
	}
	
	override fun canSpawn(world: World, x: Int, z: Int): Boolean {
		return true
	}
}