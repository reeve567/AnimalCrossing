package dev.reeve.animalcrossing

import dev.reeve.animalcrossing.dsl.skull
import dev.reeve.animalcrossing.extensions.setNormalMode
import dev.reeve.animalcrossing.extensions.setSearchMode
import dev.reeve.animalcrossing.hologram.ClientSideHologram
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

class MainMenuManager(private val animalCrossing: AnimalCrossing) : Listener {
    init {
        animalCrossing.schematicManager.mainMenu?.place(Settings.MainMenu.location)
    }

    @EventHandler
    fun onMove(e: PlayerMoveEvent) {
        if (e.player.hasMetadata("mainMenu")) {
            with(e) {
                if (to != null)
                    if (from.x != to!!.x || from.y != to!!.y || from.z != to!!.z)
                        isCancelled = true
            }
        }
    }

    fun onJoin(player: Player, move: Boolean = true) {
        val island = animalCrossing.islandManager[player.uniqueId]
        if (island != null) {
            animalCrossing.hologramManager.addHolograms(
                ClientSideHologram(
                    player,
                    "Play",
                    Settings.MainMenu.pointOne,
                    skull(Settings.HeadValues.tomNook)
                ) {
                    island.teleportToLastLocation()
                    player.setNormalMode()
                }
            )
        } else {
            animalCrossing.hologramManager.addHolograms(
                ClientSideHologram(
                    player,
                    "Create Island",
                    Settings.MainMenu.pointOne,
                    skull(Settings.HeadValues.tomNook)
                ) {
                    animalCrossing.islandManager.teleportToUnclaimedIsland(player)
                    player.setSearchMode()
                }
            )
        }
        animalCrossing.hologramManager.addHolograms(
            ClientSideHologram(
                player,
                "Settings",
                Settings.MainMenu.pointTwo,
                skull(Settings.HeadValues.settings)
            ) {
                // open settings menu
            },
            ClientSideHologram(
                player,
                "Help",
                Settings.MainMenu.pointThree,
                skull(Settings.HeadValues.help)
            ) {

            }
        )
    }
}