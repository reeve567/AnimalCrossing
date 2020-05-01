package dev.reeve.animalcrossing

import dev.reeve.animalcrossing.island.IslandManager
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ClaimCommand(private val islandManager: IslandManager) : CommandExecutor {
	init {
		Bukkit.getPluginCommand("claim")?.setExecutor(this)
	}
	
	override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
		if (sender !is Player) {
			return false
		}
		val island = islandManager.claimIsland(sender, sender.location.chunk.x, sender.location.chunk.z)
		if (island != null) {
			sender.sendMessage("You have successfully claimed an island!  Welcome to your new home! [${island.location.x}, ${island.location.z}]")
		} else {
			sender.sendMessage("You either already have an island or this island is already claimed.")
		}
		return false
	}
}