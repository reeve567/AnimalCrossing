package dev.reeve.animalcrossing.handlers

import dev.reeve.animalcrossing.AnimalCrossing
import dev.reeve.animalcrossing.PlayerLocation
import dev.reeve.animalcrossing.Tools
import dev.reeve.animalcrossing.extensions.getWalletBalance
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class JoinLeaveHandler(private val animalCrossing: AnimalCrossing) : Listener {
    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        e.player.getWalletBalance()
        e.player.inventory.setItem(0, Tools.Shovels.goldenShovel)
        e.player.gameMode = GameMode.SURVIVAL

        animalCrossing.hologramManager.onJoin(e.player)
        animalCrossing.mainMenuManager.onJoin(e.player)

        val island = animalCrossing.islandManager[e.player.uniqueId]
        if (island != null) {
            if (island.musicToggled)
                animalCrossing.musicManager.addPlayer(e.player)
            if (island.showMainMenu) {

            }
        } else {
            //send to spawn or something
        }
    }

    @EventHandler
    fun onLeave(e: PlayerQuitEvent) {
        animalCrossing.hologramManager.onLeave(e.player)

        val island = animalCrossing.islandManager[e.player.uniqueId]
        if (island != null) {
            if (island.isOnIsland(e.player.location))
                island.playerLocation = PlayerLocation(e.player.location.x, e.player.location.y, e.player.location.z)
        }
    }
}