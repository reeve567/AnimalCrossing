package dev.reeve.animalcrossing

import dev.reeve.animalcrossing.generation.WorldGenerator
import dev.reeve.animalcrossing.generation.islandSize
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

class RegenerateCommand : CommandExecutor {
    val worldGenerator = WorldGenerator(false)

    init {
        Bukkit.getPluginCommand("regenerate")?.setExecutor(this)
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            return false
        }
        val islandCornerChunkX = (sender.location.chunk.x / islandSize) * islandSize
        val islandCornerChunkZ = (sender.location.chunk.z / islandSize) * islandSize

        val world = sender.world

        for (x in islandCornerChunkX until islandCornerChunkX + islandSize) {
            for (z in islandCornerChunkZ until islandCornerChunkZ + islandSize) {
                world.populators[0].populate(world, Random(), world.getChunkAt(x, z))
            }
        }

        return true
    }
}