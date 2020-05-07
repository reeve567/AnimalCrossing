package dev.reeve.animalcrossing.handlers

import dev.reeve.animalcrossing.extensions.isShovel
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.BlockFace
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockGrowEvent
import org.bukkit.event.player.PlayerInteractEvent

class HoleHandler : Listener {
	@EventHandler
	fun onDig(e: BlockBreakEvent) {
		if (e.player.gameMode != GameMode.CREATIVE) {
			if (e.block.type == Material.GRASS_BLOCK && e.player.itemInHand.isShovel()) {
				e.isDropItems = false
			} else {
				e.isCancelled = true
			}
		}
	}
	
	@EventHandler
	fun onInteract(e: PlayerInteractEvent) {
		if (e.action == Action.RIGHT_CLICK_BLOCK) {
			if (e.clickedBlock?.type == Material.DIRT) {
				if (e.blockFace == BlockFace.UP) {
					if (e.player.itemInHand.isShovel()) {
						e.clickedBlock!!
								.world
								.playSound(e.clickedBlock!!.location, Sound.BLOCK_GRASS_PLACE, 1f, 1f)
						e.clickedBlock!!.getRelative(BlockFace.UP).setType(Material.GRASS_BLOCK, false)
					}
				}
			} else if (e.player.gameMode != GameMode.CREATIVE) e.isCancelled = true
		}
	}
	
	@EventHandler
	fun onGrow(e: BlockGrowEvent) {
		if (e.block.type == Material.DIRT) {
			e.isCancelled = true
		}
	}
}