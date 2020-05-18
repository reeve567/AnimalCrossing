package dev.reeve.animalcrossing.handlers

import dev.reeve.animalcrossing.AnimalCrossing
import dev.reeve.animalcrossing.PlayerLocation
import dev.reeve.animalcrossing.island.IslandManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerTeleportEvent

class TeleportationHandler(private val islandManager: IslandManager) : Listener {

    @EventHandler
    fun onTeleport(e: PlayerTeleportEvent) {
        if (e.cause == PlayerTeleportEvent.TeleportCause.PLUGIN) {
            val island = islandManager[e.player.uniqueId]
            if (island != null && e.to != null) {
                if (island.isOnIsland(e.from) && !island.isOnIsland(e.to!!))
                    island.playerLocation = PlayerLocation(e.from.x, e.from.y, e.from.z)
            }
        }
    }
}