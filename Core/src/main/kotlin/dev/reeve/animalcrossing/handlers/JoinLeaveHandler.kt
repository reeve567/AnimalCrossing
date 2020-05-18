package dev.reeve.animalcrossing.handlers

import com.okkero.skedule.schedule
import dev.reeve.animalcrossing.AnimalCrossing
import dev.reeve.animalcrossing.PlayerLocation
import dev.reeve.animalcrossing.Settings
import dev.reeve.animalcrossing.Tools
import dev.reeve.animalcrossing.extensions.getWalletBalance
import dev.reeve.animalcrossing.extensions.isInMainMenuMode
import dev.reeve.animalcrossing.extensions.isInSearchMode
import dev.reeve.animalcrossing.extensions.setMainMenuMode
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerTeleportEvent

class JoinLeaveHandler(private val animalCrossing: AnimalCrossing) : Listener {
    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        e.player.getWalletBalance()
        e.player.inventory.setItem(0, Tools.Shovels.goldenShovel)
        e.player.gameMode = GameMode.SURVIVAL
        e.player.isInvulnerable = true

        animalCrossing.hologramManager.onJoin(e.player)
        animalCrossing.mainMenuManager.onJoin(e.player)
        animalCrossing.settingsManager.onJoin(e.player)

        val island = animalCrossing.islandManager[e.player.uniqueId]
        if (island != null) {
            if (!island.showMainMenu) {
                island.loadInventory()
                island.teleportToLastLocation()
                return
            }
        }

        Bukkit.getScheduler().schedule(animalCrossing) {
            waitFor(2)
            e.player.teleport(
                Settings.MainMenu.location.clone().add(0.5, 0.0, 0.5),
                PlayerTeleportEvent.TeleportCause.PLUGIN
            )
        }
        e.player.setMainMenuMode()
    }

    @EventHandler
    fun onLeave(e: PlayerQuitEvent) {
        animalCrossing.hologramManager.onLeave(e.player)

        val island = animalCrossing.islandManager[e.player.uniqueId]
        if (island != null) {
            if (island.isOnIsland(e.player.location)) {
                island.playerLocation = PlayerLocation(e.player.location.x, e.player.location.y, e.player.location.z)
                if (!e.player.isInMainMenuMode() && !e.player.isInSearchMode())
                    island.saveInventory()
            }
        }
    }
}