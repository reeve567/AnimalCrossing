package dev.reeve.animalcrossing

import com.google.gson.Gson
import org.bukkit.entity.Player
import java.io.File
import java.io.FileReader
import java.util.*

class SettingsManager(private val animalCrossing: AnimalCrossing) : HashMap<UUID, PlayerSettings>() {
    private val folder = File(animalCrossing.dataFolder, "players")

    init {
        loadPlayers()
    }

    private fun loadPlayers() {
        if (!folder.exists())
            folder.mkdirs()
        for (file in folder.listFiles()!!) {
            val settings = Gson().fromJson(FileReader(file), PlayerSettings::class.java)
            put(settings.uuid, settings)
        }
    }

    fun onJoin(player: Player) {
        if (!containsKey(player.uniqueId))
            put(player.uniqueId, PlayerSettings(player.uniqueId))
    }
}