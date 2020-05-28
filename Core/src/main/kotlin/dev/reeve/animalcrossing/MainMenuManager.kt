package dev.reeve.animalcrossing

import dev.reeve.animalcrossing.dsl.skull
import dev.reeve.animalcrossing.extensions.setNormalMode
import dev.reeve.animalcrossing.extensions.setSearchMode
import dev.reeve.animalcrossing.hologram.ClientSideHologram
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerMoveEvent

class MainMenuManager(private val animalCrossing: AnimalCrossing) : Listener {
    init {
        animalCrossing.schematicManager.mainMenu?.place(Settings.MainMenu.location)
    }

    @EventHandler
    fun onMove(e: PlayerMoveEvent) {
        if (e.player.hasMetadata("mainMenu")) {
            with(e) {
                if (to != null) {
                    if (from.x != to!!.x || from.y != to!!.y || from.z != to!!.z)
                        isCancelled = true
                    if (to!!.pitch < 20 && to!!.pitch > -25) {
                        if (to!!.yaw > 20 && to!!.yaw <= 65) {
                            // settings hologram
                            setLooking(e.player, MainMenuHolograms.SETTINGS)
                        } else if (to!!.yaw > 65 && to!!.yaw <= 110) {
                            // play or create hologram
                            if (animalCrossing.islandManager.containsKey(e.player.uniqueId)) {
                                // play hologram
                                setLooking(e.player, MainMenuHolograms.PLAY)
                            } else {
                                setLooking(e.player, MainMenuHolograms.CREATE_ISLAND)
                            }
                        } else if (to!!.yaw > 110 && to!!.yaw <= 155) {
                            // help hologram
                            setLooking(e.player, MainMenuHolograms.SETTINGS)
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    fun onClick(e: PlayerInteractAtEntityEvent) {
        if (e.player.hasMetadata("mainMenu") && !e.isCancelled) {
            if (e.rightClicked is ArmorStand) {
                animalCrossing.hologramManager.getPairsClientSideHolograms().forEach { entry ->
                    entry.value.firstOrNull { it.uuid == e.rightClicked.uniqueId }?.apply {
                        if (entry.key == e.player.uniqueId) {
                            e.player.apply(block)
                        } else {
                            if (hologramType == MainMenuHolograms.PLAY || hologramType == MainMenuHolograms.CREATE_ISLAND) {
                                if (animalCrossing.islandManager.containsKey(e.player.uniqueId)) {
                                    animalCrossing.hologramManager.getClientSideHologram(e.player.uniqueId)?.first { it.hologramType == MainMenuHolograms.PLAY }?.also {
                                        e.player.apply(it.block)
                                    }
                                } else {
                                    animalCrossing.hologramManager.getClientSideHologram(e.player.uniqueId)?.first { it.hologramType == MainMenuHolograms.CREATE_ISLAND }?.also {
                                        e.player.apply(it.block)
                                    }
                                }
                            } else {
                                animalCrossing.hologramManager.getClientSideHologram(e.player.uniqueId)?.first { it.hologramType == hologramType }?.also {
                                    e.player.apply(it.block)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setLooking(player: Player, hologram: MainMenuHolograms) {
        val holograms = animalCrossing.hologramManager.getClientSideHologram(player.uniqueId)
        if (holograms != null) {
            holograms.find { it.hologramType == hologram }?.apply {
                looking = true
            }
            holograms.filter { it.hologramType != hologram }.apply {
                forEach {
                    it.looking = false
                }
            }
        }
    }

    fun onJoin(player: Player) {
        val island = animalCrossing.islandManager[player.uniqueId]
        if (island != null) {
            animalCrossing.hologramManager.addClientSideHologram(
                ClientSideHologram(
                    player,
                    "Play",
                    Settings.MainMenu.pointOne,
                    MainMenuHolograms.PLAY,
                    skull(Settings.HeadValues.tomNook)
                ) {
                    island.loadInventory()
                    island.teleportToLastLocation()
                    player.setNormalMode()
                } to player.uniqueId
            )
        } else {
            animalCrossing.hologramManager.addClientSideHologram(
                ClientSideHologram(
                    player,
                    "Create Island",
                    Settings.MainMenu.pointOne,
                    MainMenuHolograms.CREATE_ISLAND,
                    skull(Settings.HeadValues.tomNook)
                ) {
                    animalCrossing.islandManager.teleportToUnclaimedIsland(player)
                    player.setSearchMode()
                } to player.uniqueId
            )
        }
        animalCrossing.hologramManager.addClientSideHologram(
            ClientSideHologram(
                player,
                "Settings",
                Settings.MainMenu.pointTwo,
                MainMenuHolograms.SETTINGS,
                skull(Settings.HeadValues.settings)
            ) {
                // open settings menu
            } to player.uniqueId,
            ClientSideHologram(
                player,
                "Help",
                Settings.MainMenu.pointThree,
                MainMenuHolograms.HELP,
                skull(Settings.HeadValues.help)
            ) {

            } to player.uniqueId
        )
    }

    enum class MainMenuHolograms {
        PLAY,
        CREATE_ISLAND,
        SETTINGS,
        HELP
    }
}