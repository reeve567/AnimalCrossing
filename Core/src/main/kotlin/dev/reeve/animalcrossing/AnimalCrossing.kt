package dev.reeve.animalcrossing

import com.okkero.skedule.schedule
import dev.reeve.animalcrossing.handlers.*
import dev.reeve.animalcrossing.hologram.HologramManager
import dev.reeve.animalcrossing.island.ClaimCommand
import dev.reeve.animalcrossing.island.IslandManager
import dev.reeve.animalcrossing.music.MusicCommand
import dev.reeve.animalcrossing.music.MusicManager
import dev.reeve.animalcrossing.schematic.SchematicCommand
import dev.reeve.animalcrossing.schematic.SchematicManager
import org.bukkit.Bukkit
import org.bukkit.Difficulty
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class AnimalCrossing : JavaPlugin() {
    lateinit var islandManager: IslandManager
    lateinit var musicManager: MusicManager
    lateinit var schematicManager: SchematicManager
    lateinit var hologramManager: HologramManager
    lateinit var mainMenuManager: MainMenuManager

    companion object {
        lateinit var instance: AnimalCrossing
    }

    override fun onEnable() {
        instance = this
        islandManager = IslandManager(this)
        musicManager = MusicManager(this)
        schematicManager = SchematicManager(this)
        hologramManager = HologramManager(this)
        mainMenuManager = MainMenuManager(this)

        registerListeners(
            HoleHandler(),
            WalletHandler(),
            JoinLeaveHandler(this),
            TreeHandler(islandManager),
            InfoTool(),
            SchematicToolHandler(schematicManager),
            TeleportationHandler(islandManager),
            mainMenuManager
        )

        ClaimCommand(islandManager)
        MusicCommand(islandManager)
        SchematicCommand(schematicManager)

        val world = Bukkit.getWorld("world")!!
        world.difficulty = Difficulty.PEACEFUL
    }

    override fun onDisable() {
        hologramManager.onDisable()
        islandManager.saveIslands()
    }

    private fun registerListeners(vararg listeners: Listener) {
        listeners.forEach {
            Bukkit.getPluginManager().registerEvents(it, this)
        }
    }
}