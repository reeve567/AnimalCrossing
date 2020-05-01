package dev.reeve.animalcrossing.handlers

import dev.reeve.animalcrossing.Tools
import dev.reeve.animalcrossing.extensions.getWalletBalance
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class JoinHandler : Listener {
	@EventHandler
	fun onJoin(e: PlayerJoinEvent) {
		e.player.getWalletBalance()
		e.player.inventory.setItem(0, Tools.Shovels.goldenShovel)
		e.player.gameMode = GameMode.SURVIVAL
		e.player.teleport(Location(Bukkit.getWorld("world"), 0.0, 60.0, 0.0))
	}
}