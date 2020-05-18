package dev.reeve.animalcrossing.schematic

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import dev.reeve.animalcrossing.AnimalCrossing
import dev.reeve.animalcrossing.BlockLocation
import org.bukkit.Location
import java.io.File
import java.io.InputStreamReader
import java.util.*
import kotlin.collections.HashMap

class SchematicManager(private val animalCrossing: AnimalCrossing) {
    var tent: Schematic? = getSchem("tent")
    val houseOne = getSchem("houseOne")
    val mainMenu = getSchem("mainMenu")

    private fun getSchem(name: String): Schematic? {
        return try {
            Gson().fromJson(
                InputStreamReader(animalCrossing.getResource("schematics/$name.json")),
                Schematic::class.java
            )
        } catch (e: JsonSyntaxException) {
            // should never happen you dumbass
            println(InputStreamReader(animalCrossing.getResource("schematics/tent.json")).readText())
            e.printStackTrace()
            null
        }
    }

    var positionOne: Location? = null
    var positionTwo: Location? = null

    val placing = HashMap<UUID, BlockLocation>()
    val blocks = HashMap<UUID, List<BlockLocation>>()

    fun save(schem: Schematic) {
        val file = File(AnimalCrossing.instance.dataFolder, "${schem.name}.json")
        if (file.exists()) {
            file.delete()
        }
        file.createNewFile()
        file.bufferedWriter().apply {
            write(GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create().toJson(schem))
            close()
        }
    }
}