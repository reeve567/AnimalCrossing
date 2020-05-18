package dev.reeve.animalcrossing.island

import com.okkero.skedule.schedule
import dev.reeve.animalcrossing.AnimalCrossing
import dev.reeve.animalcrossing.PlayerLocation
import dev.reeve.animalcrossing.Settings
import dev.reeve.animalcrossing.Tools
import dev.reeve.animalcrossing.extensions.isInMainMenuMode
import dev.reeve.animalcrossing.extensions.isInSearchMode
import dev.reeve.animalcrossing.generator.islandSize
import dev.reeve.animalcrossing.island.trees.FruitType
import dev.reeve.animalcrossing.island.trees.IslandTree
import dev.reeve.animalcrossing.island.trees.IslandTreeLocation
import dev.reeve.animalcrossing.island.trees.IslandTrees
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*

class Island(
    val location: IslandLocation,
    val owner: UUID,
    private var nativeFruitType: FruitType? = null,
    val islandId: UUID = UUID.randomUUID()
) {
    val trees = IslandTrees()
    var bells = 0
    var playerLocation = PlayerLocation(0.0, 0.0, 0.0)
    var showMainMenu = true
    var inventory = Array<ItemStack?>(36) { null }
    var armor = Array<ItemStack?>(4) { null }

    @Transient
    private val world = Bukkit.getWorld("world")!!

    init {
        kickOffPlayers()
        if (nativeFruitType == null) {
            nativeFruitType = FruitType.values().random()
            findTrees()
        }
        loadInventory()
    }

    fun teleportToLastLocation() {
        getOwnerPlayer()?.teleport(
            Location(
                Settings.world,
                playerLocation.x,
                playerLocation.y,
                playerLocation.z
            )
        )
    }

    private fun kickOffPlayers() {
        for (player in Bukkit.getOnlinePlayers()) {
            if (isOnIsland(player.location)) {
                if (player.uniqueId != owner) {
                    if (player.isInSearchMode()) {
                        AnimalCrossing.instance.islandManager.teleportToUnclaimedIsland(player)
                    } else if (player.isInMainMenuMode()) {
                        player.teleport(Settings.MainMenu.location)
                    }
                }
            }
        }
    }

    private fun findTrees() {
        Bukkit.getScheduler().schedule(AnimalCrossing.instance) {
            val time = System.currentTimeMillis()
            for (chunkX in 0 until islandSize) {
                waitFor(5)
                for (chunkZ in 0 until islandSize) {
                    if (chunkZ % 5 == 0)
                        waitFor(1)
                    val chunk = Location(world, (location.x + chunkX) * 16.0, 1.0, (location.z + chunkZ) * 16.0).chunk
                    val highest = world.getHighestBlockYAt(chunk.getBlock(0, 1, 0).location) - 2
                    val hasWater = Array(16) {
                        Array(16) {
                            false
                        }
                    }
                    chunk@ for (y in 0 until 10) {
                        for (x in 0 until 16) {
                            for (z in 0 until 16) {
                                if (chunk.getBlock(x, y + highest, z).type == Material.WATER)
                                    hasWater[x][z] = true
                                else if (hasWater[x][z])
                                    continue

                                val block = chunk.getBlock(x, y + highest, z)
                                if (block.type == Material.OAK_LOG) {
                                    var localY = 0
                                    do {
                                        localY++
                                    } while (chunk.getBlock(x, y + highest - localY, z).type == Material.OAK_LOG)

                                    trees.add(
                                        IslandTree(
                                            IslandTreeLocation(
                                                (location.x + chunkX) * 16 + x,
                                                highest + y - (localY - 1),
                                                (location.z + chunkZ) * 16 + z,
                                                world.name
                                            ),
                                            nativeFruitType!!
                                        )
                                    )
                                    break@chunk
                                }
                            }
                        }
                    }
                }
            }
            Bukkit.getPlayer(owner)!!.sendMessage("done ${(System.currentTimeMillis() - time) / 1000.0}")
        }
    }

    fun isIsland(x: Int, z: Int): Boolean {
        return location.x == (x / islandSize) * islandSize && location.z == (z / islandSize) * islandSize
    }

    fun isOnIsland(location: Location): Boolean {
        return isIsland(location.chunk.x, location.chunk.z)
    }

    fun loadInventory() {
        inventory.forEachIndexed { index, itemStack ->
            if (index != Tools.walletSlot)
                getOwnerPlayer()?.inventory?.setItem(index, itemStack)
        }
        getOwnerPlayer()?.inventory?.setArmorContents(armor)
    }

    fun saveInventory() {
        for (i in 0 until 36) {
            if (i != Tools.walletSlot)
                getOwnerPlayer()?.inventory?.contents?.also {
                    inventory[i] = it[i]
                }
        }
        armor = getOwnerPlayer()?.inventory?.armorContents as Array<ItemStack?>
    }

    private fun getOwnerPlayer(): Player? {
        return Bukkit.getPlayer(owner)
    }
}