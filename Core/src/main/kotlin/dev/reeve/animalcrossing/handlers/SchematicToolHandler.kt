package dev.reeve.animalcrossing.handlers

import dev.reeve.animalcrossing.schematic.SchematicManager
import dev.reeve.animalcrossing.Tools
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class SchematicToolHandler(private val schematicManager: SchematicManager) : Listener {

    @EventHandler
    fun onClick(e: PlayerInteractEvent) {
        if (e.hasItem()) {
            if (e.item!!.type == Material.FERMENTED_SPIDER_EYE) {
                e.isCancelled = true
                if (e.action == Action.RIGHT_CLICK_BLOCK) {
                    schematicManager.positionTwo = e.clickedBlock!!.location
                } else if (e.action == Action.LEFT_CLICK_BLOCK) {
                    schematicManager.positionOne = e.clickedBlock!!.location
                }
            }
            if (e.item!!.type == Tools.Blueprints.type) {
                e.isCancelled = true
                if (e.action == Action.LEFT_CLICK_AIR) {
                    if (e.player.isSneaking) {
                        // confirm
                    } else {
                        // set area
                        val schem = when {
                            e.item!!.hashCode() == Tools.Blueprints.tent.hashCode() -> {
                                schematicManager.tent
                            }
                            e.item!!.hashCode() == Tools.Blueprints.houseOne.hashCode() -> {
                                schematicManager.houseOne
                            }
                            else -> null
                        }

                        val facing = e.player.facing

                        var width: Int
                        var length: Int

                        when (facing) {
                            BlockFace.NORTH -> {

                            }
                        }

                    }
                }
            }
        }
    }
}