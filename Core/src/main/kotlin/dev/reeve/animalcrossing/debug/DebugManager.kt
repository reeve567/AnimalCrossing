package dev.reeve.animalcrossing.debug

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dev.reeve.animalcrossing.AnimalCrossing
import java.io.File

class DebugManager(private val animalCrossing: AnimalCrossing) {
    private var treeLog = TreeLog()

    init {
        loadLogs()
    }

    private fun loadLogs() {
        val file = File(animalCrossing.dataFolder, "treesLog.json")
        if (file.exists()) {
            treeLog = Gson().fromJson(file.reader(), TreeLog::class.java)
        }
    }

    fun addTreeLogEvent(version: Int, time: Double) {
        treeLog.addEvent(version, time)
        val file = File(animalCrossing.dataFolder, "treesLog.json")
        if (!file.exists())
            file.createNewFile()
        file.writeText(GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(treeLog))
    }

}