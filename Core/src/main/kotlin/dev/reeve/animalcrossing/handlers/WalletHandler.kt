package dev.reeve.animalcrossing.handlers

import dev.reeve.animalcrossing.extensions.getWalletBalance
import dev.reeve.animalcrossing.extensions.isWallet
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent

class WalletHandler : Listener {
	@EventHandler
	fun onUse(e: PlayerInteractEvent) {
		if (e.item.isWallet()) {
			e.isCancelled = true
			e.player.sendMessage("§7Bells §8§l» §6${e.player.getWalletBalance()}")
		}
	}

	@EventHandler
	fun onClick(e: InventoryClickEvent) {
		if (e.currentItem.isWallet()) {
			e.isCancelled = true
		}
	}

	@EventHandler
	fun onDrop(e: PlayerDropItemEvent) {
		if (e.itemDrop.itemStack.isWallet()) {
			e.isCancelled = true
			// TODO : make some items on the ground if they try to drop it
		}
	}
}