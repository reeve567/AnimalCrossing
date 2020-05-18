package dev.reeve.animalcrossing.music

import dev.reeve.animalcrossing.AnimalCrossing
import dev.reeve.animalcrossing.island.IslandManager
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class MusicCommand(private val islandManager: IslandManager) : CommandExecutor {
    init {
        Bukkit.getPluginCommand("music")?.setExecutor(this)
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            return false
        }
        val island = islandManager[sender.uniqueId]
        if (island != null) {
            island.musicToggled = !island.musicToggled
            when (island.musicToggled) {
                true -> {
                    sender.sendMessage("§6Music toggled on")
                }
                false -> {
                    sender.sendMessage("§6Music toggled off")
                }
            }
        }
        return false
    }
}