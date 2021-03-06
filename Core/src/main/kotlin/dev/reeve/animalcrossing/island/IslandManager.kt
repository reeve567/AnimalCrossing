package dev.reeve.animalcrossing.island

import dev.reeve.animalcrossing.AnimalCrossing
import dev.reeve.animalcrossing.GsonFactory
import dev.reeve.animalcrossing.PlayerLocation
import dev.reeve.animalcrossing.Settings
import dev.reeve.animalcrossing.extensions.setWorldBorders
import dev.reeve.animalcrossing.generator.islandSize
import dev.reeve.animalcrossing.generator.waterLevel
import dev.reeve.animalcrossing.utility.up
import org.bukkit.Bukkit
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.entity.Player
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.*
import kotlin.random.Random
import kotlin.random.nextInt

class IslandManager(animalCrossing: AnimalCrossing) : HashMap<UUID, Island>() {
    private val folder = File(animalCrossing.dataFolder, "islands")
    private var searchArea = 4

    init {
        loadIslands()
        setSearchArea()
    }

    private fun setSearchArea() {
        var i = 4
        while (i * i < size * 2) {
            i++
        }
        searchArea = i
    }

    fun teleportToUnclaimedIsland(player: Player) {
        setSearchArea()
        var island: Island?
        var location: IslandLocation
        do {
            location = IslandLocation(Random.nextInt(0 until searchArea), Random.nextInt(0 until searchArea))
            island = getIsland(location)
        } while (island != null)

        teleportToIsland(player, location)
    }

    fun teleportToIsland(player: Player, islandLocation: IslandLocation) {
        val islandCenterX = (islandSize / 2.0) + islandLocation.x * islandSize
        val islandCenterZ = (islandSize / 2.0) + islandLocation.z * islandSize

        player.setWorldBorders(islandSize * 16.0, PlayerLocation(islandCenterX * 16, 0.0, islandCenterZ * 16))

        var location: Location? = null
        do {
            for (y in 0..7) {
                val loc = Location(
                    Settings.world,
                    islandCenterX * 16 + Random.nextInt(-30..30),
                    waterLevel.toDouble() + y,
                    islandCenterZ * 16 + Random.nextInt(-30..30)
                )
                if (loc.block.type.isSolid) {
                    if (loc.up().block.type.isAir && loc.up().up().block.type.isAir) {
                        location = loc.up()
                        break
                    }
                }
            }
        } while (location == null)
        player.teleport(location)
    }

    private fun loadIslands() {
        if (!folder.exists())
            folder.mkdirs()
        for (file in folder.listFiles()!!) {
            val island = GsonFactory.prettyGson!!.fromJson(FileReader(file), Island::class.java)
            put(island.owner, island)
        }
    }

    fun saveIslands() {
        println("Saving Islands")
        forEach {
            if (Bukkit.getPlayer(it.value.owner) != null) {
                it.value.saveInventory()
            }

            val file = File(folder, "${it.value.islandId}.json")
            if (!file.exists())
                file.createNewFile()
            val writer = FileWriter(file)
            writer.write(
                GsonFactory.prettyGson!!.toJson(it.value)
            )
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

    fun getIsland(islandLocation: IslandLocation): Island? {
        return getIsland(islandLocation.x * islandSize, islandLocation.z * islandSize)
    }

    /**
     * @param x is the chunk x not the player x
     * @param z see above
     */
    fun claimIsland(player: Player, x: Int, z: Int): Island? {
        if (!containsKey(player.uniqueId) && getIsland(x, z) == null) {
            val island =
                Island(IslandLocation((x / islandSize) * islandSize, (z / islandSize) * islandSize), player.uniqueId)
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