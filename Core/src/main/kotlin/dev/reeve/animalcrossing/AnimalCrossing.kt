package dev.reeve.animalcrossing

import dev.reeve.animalcrossing.handlers.HoleHandler
import dev.reeve.animalcrossing.handlers.JoinHandler
import dev.reeve.animalcrossing.handlers.TreeHandler
import dev.reeve.animalcrossing.handlers.WalletHandler
import dev.reeve.animalcrossing.island.IslandManager
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class AnimalCrossing : JavaPlugin() {
    private lateinit var islandManager: IslandManager

    companion object {
        lateinit var instance: AnimalCrossing
    }

    override fun onEnable() {
        instance = this
        islandManager = IslandManager(this)
        registerListeners(
            HoleHandler(),
            WalletHandler(),
            JoinHandler(),
            TreeHandler(islandManager)
        )

        ClaimCommand(islandManager)
    }

    override fun onDisable() {
        islandManager.saveIslands()
    }

    private fun registerListeners(vararg listeners: Listener) {
        listeners.forEach {
            Bukkit.getPluginManager().registerEvents(it, this)
        }
    }
}