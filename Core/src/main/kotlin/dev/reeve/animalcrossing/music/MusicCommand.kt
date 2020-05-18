package dev.reeve.animalcrossing.music

import dev.reeve.animalcrossing.SettingsManager
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class MusicCommand(private val settingsManager: SettingsManager) : CommandExecutor {
    init {
        Bukkit.getPluginCommand("music")?.setExecutor(this)
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            return false
        }
        val playerSettings = settingsManager[sender.uniqueId]
        if (playerSettings != null) {
            playerSettings.music = !playerSettings.music
            when (playerSettings.music) {
                true -> {
                    sender.sendMessage("§6Music toggled on")
                }
                false -> {
                    sender.sendMessage("§6Music toggled off")
                }
            }
        }
        return true
    }
}