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
import kotlin.collections.HashMap

class IslandManager(animalCrossing: AnimalCrossing) : HashMap<UUID, Island>() {
	private val folder = File(animalCrossing.dataFolder, "islands")
	
	init {
		loadIslands()
	}
	
	private fun loadIslands() {
		if (!folder.exists())
			folder.mkdirs()
		for (file in folder.listFiles()!!) {
			val island = Gson().fromJson(FileReader(file), Island::class.java)
			put(island.owner, island)
		}
	}
	
	 fun saveIslands() {
		forEach {
			val file = File(folder, "${it.value.islandId}.json")
			if (!file.exists())
				file.createNewFile()
			val writer = FileWriter(file)
			writer.write(GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create().toJson(it.value))
			writer.close()
		}
	}
	
	/**
	 * @param x is the chunk x not the player x
	 * @param z see above
	 * @return island which may or may not exist
	 */
	fun getIsland(x: Int, z: Int): Island? {
		return values.firstOrNull { island ->
			island.isIsland(x, z)
		}
	}
	
	/**
	 * @param x is the chunk x not the player x
	 * @param z see above
	 */
	fun claimIsland(player: Player, x: Int, z: Int): Island? {
		if (!containsKey(player.uniqueId) && getIsland(x, z) == null) {
			val island = Island(IslandLocation( (x / islandSize) * islandSize, (z / islandSize) * islandSize), player.uniqueId)
			put(player.uniqueId, island)
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