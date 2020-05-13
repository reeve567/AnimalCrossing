package dev.reeve.animalcrossing.island

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dev.reeve.animalcrossing.AnimalCrossing
import dev.reeve.animalcrossing.generator.islandSize
import org.bukkit.Chunk
import org.bukkit.entity.Player
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.*

class IslandManager(animalCrossing: AnimalCrossing) : ArrayList<Island>() {
	private val folder = File(animalCrossing.dataFolder, "islands")
	
	init {
		loadIslands()
	}
	
	private fun loadIslands() {
		if (!folder.exists())
			folder.mkdirs()
		for (file in folder.listFiles()!!) {
			add(Gson().fromJson(FileReader(file), Island::class.java))
		}
	}
	
	 fun saveIslands() {
		forEach {
			val file = File(folder, "${it.islandId}.json")
			if (!file.exists())
				file.createNewFile()
			val writer = FileWriter(file)
			writer.write(GsonBuilder().setPrettyPrinting().create().toJson(it))
			writer.close()
		}
	}
	
	/**
	 * @param x is the chunk x not the player x
	 * @param z see above
	 * @return island which may or may not exist
	 */
	fun getIsland(x: Int, z: Int): Island? {
		return firstOrNull { island ->
			island.isIsland(x, z)
		}
	}
	
	fun getIsland(uuid: UUID): Island? {
		return firstOrNull {
			it.islandId == uuid
		}
	}
	
	fun hasIsland(uuid: UUID): Boolean {
		return firstOrNull { it.owner == uuid } != null
	}
	
	/**
	 * @param x is the chunk x not the player x
	 * @param z see above
	 */
	fun claimIsland(player: Player, x: Int, z: Int): Island? {
		if (!hasIsland(player.uniqueId) && getIsland(x, z) == null) {
			val island = Island(IslandLocation( (x / islandSize) * islandSize, (z / islandSize) * islandSize), player.uniqueId)
			add(island)
			return island
		}
		return null
	}
	
	fun claimIsland(player: Player, chunk: Chunk): Island? {
		return claimIsland(player, chunk.x, chunk.z)
	}
	
	fun getIsland(chunk: Chunk): Island? {
		return getIsland(chunk.x, chunk.z)
	}
}