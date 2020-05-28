package dev.reeve.animalcrossing.hologram

import dev.reeve.animalcrossing.AnimalCrossing
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class HologramManager(private val animalCrossing: AnimalCrossing) {
    private val world = Bukkit.getWorld("world")!!
    private val holograms = ArrayList<Hologram>()
    private val clientSideHolograms = HashMap<UUID, ArrayList<ClientSideHologram>>()

    fun addHolograms(vararg holograms: Hologram) {
        holograms.forEach {
            if (it.register)
                Bukkit.getPluginManager().registerEvents(it, animalCrossing)
        }
        this.holograms.addAll(holograms)
    }

    fun addClientSideHologram(vararg hologram: Pair<ClientSideHologram, UUID>) {
        hologram.forEach {
            if (clientSideHolograms.containsKey(it.second)) {
                clientSideHolograms[it.second]!!.add(it.first)
            } else {
                clientSideHolograms[it.second] = arrayListOf(it.first)
            }
        }
    }

    fun getHologram(uuid: UUID): Hologram? {
        return holograms.firstOrNull { it.uuid == uuid }
    }

    fun getClientSideHologram(uuid: UUID): ArrayList<ClientSideHologram>? {
        return clientSideHolograms[uuid]
    }

    fun getAllClientSideHolograms() : MutableCollection<ArrayList<ClientSideHologram>> {
        return clientSideHolograms.values
    }

    fun getPairsClientSideHolograms(): MutableSet<MutableMap.MutableEntry<UUID, ArrayList<ClientSideHologram>>> {
        return clientSideHolograms.entries
    }

    fun onJoin(player: Player) {
        clientSideHolograms.forEach {
            if (it.key != player.uniqueId) {
                it.value.forEach { hologram ->
                    hologram.deletePlayer(player)
                }
            }
        }
    }

    fun onLeave(player: Player) {
        clientSideHolograms[player.uniqueId]?.forEach {
            it.destroy()
        }
        clientSideHolograms.remove(player.uniqueId)
    }

    fun onDisable() {
        holograms.forEach {
            it.destroy()
        }
        clientSideHolograms.forEach {
            it.value.forEach { hologram ->
                hologram.destroy()
            }
        }
    }
}