package dev.reeve.animalcrossing.handlers

import dev.reeve.animalcrossing.Tools
import dev.reeve.animalcrossing.extensions.getWalletBalance
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.plugin.java.JavaPlugin

class JoinHandler(private val plugin: JavaPlugin) : Listener {
    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        e.player.getWalletBalance()
        e.player.inventory.setItem(0, Tools.Shovels.goldenShovel)
        e.player.gameMode = GameMode.SURVIVAL
        if (!e.player.hasMetadata("joined-before")) {
            e.player.teleport(Location(Bukkit.getWorld("world"), 0.0, 60.0, 0.0))
            e.player.setMetadata("joined-before", FixedMetadataValue(plugin, true))
        }
    }
}