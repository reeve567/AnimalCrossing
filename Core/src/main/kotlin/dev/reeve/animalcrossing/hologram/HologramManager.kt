package dev.reeve.animalcrossing.hologram

import dev.reeve.animalcrossing.AnimalCrossing
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*
import kotlin.collections.ArrayList

class HologramManager(private val animalCrossing: AnimalCrossing) {
    private val world = Bukkit.getWorld("world")!!
    private val holograms = ArrayList<Hologram>()

    fun addHolograms(vararg holograms: Hologram) {
        holograms.forEach {
            Bukkit.getPluginManager().registerEvents(it, animalCrossing)
        }
        this.holograms.addAll(holograms)
    }

    fun getHologram(uuid: UUID): Hologram? {
        return holograms.firstOrNull { it.uuid == uuid }
    }

    fun onJoin(player: Player) {
        holograms.forEach {
            if (it is ClientSideHologram) {
                it.deletePlayer(player)
            }
        }
    }

    fun onLeave(player: Player) {
        holograms.forEach {
            if (it is ClientSideHologram) {
                if (it.player.uniqueId == player.uniqueId)
                    it.destroy()
            }
        }
    }

    fun onDisable() {
        holograms.forEach {
            it.destroy()
        }
    }
}