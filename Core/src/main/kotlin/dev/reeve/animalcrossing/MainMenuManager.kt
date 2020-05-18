package dev.reeve.animalcrossing

import com.okkero.skedule.schedule
import dev.reeve.animalcrossing.dsl.skull
import dev.reeve.animalcrossing.hologram.ClientSideHologram
import org.bukkit.Bukkit
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.metadata.FixedMetadataValue

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
        if (move) {
            Bukkit.getScheduler().schedule(animalCrossing) {
                waitFor(2)
                player.teleport(
                    Settings.MainMenu.location.clone().add(0.5, 0.0, 0.5),
                    PlayerTeleportEvent.TeleportCause.PLUGIN
                )
            }
            player.setMetadata("mainMenu", FixedMetadataValue(animalCrossing, true))
        }
        val island = animalCrossing.islandManager[player.uniqueId]
        if (island != null) {
            animalCrossing.hologramManager.addHolograms(
                ClientSideHologram(
                    player,
                    "Play",
                    Settings.MainMenu.pointOne,
                    skull(Settings.HeadValues.tomNook)
                ) {

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