package dev.reeve.animalcrossing.handlers

import dev.reeve.animalcrossing.island.IslandManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class TreeHandler(private val islandManager: IslandManager) : Listener {
	init {
		println("Tree handler started")
	}

	@EventHandler
	fun onInteract(e: PlayerInteractEvent) {
		if (e.action == Action.RIGHT_CLICK_BLOCK) {
			if (!e.hasItem()) {
				println("shake")
				islandManager.getIsland(e.clickedBlock!!.chunk)?.trees?.shakeTree(e.clickedBlock!!.location)
			}
		}
	}
}