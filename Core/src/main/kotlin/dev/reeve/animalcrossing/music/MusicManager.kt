package dev.reeve.animalcrossing.music

import dev.reeve.animalcrossing.AnimalCrossing
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

class MusicManager(animalCrossing: AnimalCrossing) {
    private val theme = Song(animalCrossing, "ThemeSong")

    init {
        for (player in Bukkit.getOnlinePlayers()) {
            val island = animalCrossing.islandManager[player.uniqueId]
            if (island != null && island.musicToggled)
                theme.addPlayer(player)
            else
                theme.addPlayer(player)
        }
    }

    fun addPlayer(player: Player) {
        addPlayer(player.uniqueId)
    }

    fun addPlayer(uuid: UUID) {
        theme.addPlayer(uuid)
    }

    fun removePlayer(player: Player) {
        removePlayer(player.uniqueId)
    }

    fun removePlayer(uuid: UUID) {
        theme.removePlayer(uuid)
    }
}