package dev.reeve.animalcrossing.handlers

import org.bukkit.Material
import org.bukkit.block.data.type.Chest
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class InfoTool : Listener {
    @EventHandler
    fun onHit(e: PlayerInteractEvent) {
        if (e.action == Action.LEFT_CLICK_BLOCK && e.hasItem() && e.item!!.type == Material.STICK) {
            if (e.clickedBlock!!.type == Material.CHEST) {

                val block = e.clickedBlock!!.blockData as Chest
                e.player.sendMessage("Block:{Facing:${block.facing}:${block.facing.ordinal},Data:${e.clickedBlock!!.state.blockData.asString}}")
            }
        }
    }
}