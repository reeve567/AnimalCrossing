package dev.reeve.animalcrossing.schematic

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SchematicCommand(private val schematicManager: SchematicManager) : CommandExecutor {
    init {
        Bukkit.getPluginCommand("schem")?.setExecutor(this)
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (schematicManager.positionOne != null && schematicManager.positionTwo != null && sender is Player) {
            val schem = Schematic(args[0])
            schem.setBlocks(schematicManager.positionOne!!, schematicManager.positionTwo!!, sender.location)
            schematicManager.save(schem)
        }
        return false
    }
}