package dev.reeve.animalcrossing.island

import com.okkero.skedule.schedule
import dev.reeve.animalcrossing.AnimalCrossing
import dev.reeve.animalcrossing.PlayerLocation
import dev.reeve.animalcrossing.Settings
import dev.reeve.animalcrossing.Tools
import dev.reeve.animalcrossing.extensions.isInMainMenuMode
import dev.reeve.animalcrossing.extensions.isInSearchMode
import dev.reeve.animalcrossing.extensions.setWorldBorders
import dev.reeve.animalcrossing.generator.islandSize
import dev.reeve.animalcrossing.generator.waterLevel
import dev.reeve.animalcrossing.island.npc.NPC
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
    private var inventory = Array<ItemStack?>(36) { null }
    private var armor = Array<ItemStack?>(4) { null }

    @Transient
    private val world = Bukkit.getWorld("world")!!
    private val npcs = ArrayList<NPC>()

    init {
        kickOffPlayers()
        createNPCs()
        if (nativeFruitType == null) {
            nativeFruitType = FruitType.values().random()
            findTrees()
        }
        loadInventory()
    }

    private fun createNPCs() {
        val test = NPC("Tom Nook", getOwnerPlayer()!!.location, Settings.SkinValues.tomNook)
        //val test = NPC("test", world.getHighestBlockAt(getCenter().x.toInt(), getCenter().z.toInt()).location)
        test.sendPacket(getOwnerPlayer()!!)
        npcs.add(test)
    }

    fun teleportToLastLocation() {
        val player = getOwnerPlayer()
        if (player != null) {

            player.teleport(
                Location(
                    Settings.world,
                    playerLocation.x,
                    playerLocation.y,
                    playerLocation.z
                )
            )

            val islandCenterX = (islandSize / 2.0) + location.x
            val islandCenterZ = (islandSize / 2.0) + location.z

            player.setWorldBorders(
                islandSize * 16.0,
                PlayerLocation(islandCenterX * 16, 0.0, islandCenterZ * 16)
            )
        }
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
        findTreesVersion2()
    }

    private fun findTreesVersion1() {
        Bukkit.getScheduler().schedule(AnimalCrossing.instance) {
            var sent25 = false
            var sent50 = false
            var sent75 = false

            val time = System.currentTimeMillis()
            for (chunkX in 0 until islandSize) {
                waitFor(5)
                for (chunkZ in 0 until islandSize) {
                    if (chunkZ % 2 == 0)
                        waitFor(1)
                    val chunk = Location(world, (location.x + chunkX) * 16.0, 1.0, (location.z + chunkZ) * 16.0).chunk
                    val noGood = Array(16) {
                        Array(16) {
                            false
                        }
                    }
                    chunk@ for (y in 2 until 10) {
                        for (x in 0 until 16) {
                            for (z in 0 until 16) {
                                if (chunk.getBlock(x, y + waterLevel, z).type == Material.AIR)
                                    noGood[x][z] = true
                                else if (noGood[x][z])
                                    continue

                                val block = chunk.getBlock(x, y + waterLevel, z)
                                if (block.type == Material.OAK_LOG) {
                                    var localY = 0
                                    do {
                                        localY++
                                    } while (chunk.getBlock(x, y + waterLevel - localY, z).type == Material.OAK_LOG)

                                    trees.add(
                                        IslandTree(
                                            IslandTreeLocation(
                                                (location.x + chunkX) * 16 + x,
                                                waterLevel + y - (localY - 1),
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

                if (!sent25) {
                    if (chunkX.toDouble() / islandSize >= 0.25) {
                        sent25 = true
                        getOwnerPlayer()?.sendMessage("§6Loading: ${"%.2f".format((chunkX.toDouble() / islandSize) * 100)}%")
                    }
                }

                if (!sent50) {
                    if (chunkX.toDouble() / islandSize >= 0.50) {
                        sent50 = true
                        getOwnerPlayer()?.sendMessage("§6Loading: ${"%.2f".format((chunkX.toDouble() / islandSize) * 100)}%")
                    }
                }

                if (!sent75) {
                    if (chunkX.toDouble() / islandSize >= 0.75) {
                        sent75 = true
                        getOwnerPlayer()?.sendMessage("§6Loading: ${"%.2f".format((chunkX.toDouble() / islandSize) * 100)}%")
                    }
                }
            }
            Bukkit.getPlayer(owner)!!.sendMessage("done ${(System.currentTimeMillis() - time) / 1000.0}")
            AnimalCrossing.instance.debugManager.addTreeLogEvent(
                1,
                (System.currentTimeMillis() - time) / 1000.0
            )
        }
    }

    private fun findTreesVersion2() {
        Bukkit.getScheduler().schedule(AnimalCrossing.instance) {
            var sent25 = false
            var sent50 = false
            var sent75 = false

            val time = System.currentTimeMillis()
            for (chunkX in 0 until islandSize) {
                waitFor(5)
                for (chunkZ in 0 until islandSize) {
                    if (chunkZ % 2 == 0)
                        waitFor(1)
                    val chunk = Location(world, (location.x + chunkX) * 16.0, 1.0, (location.z + chunkZ) * 16.0).chunk

                    chunk@ for (x in 0 until 16) {
                        for (z in 0 until 16) {
                            y@ for (y in 2 until 10) {
                                if (chunk.getBlock(x, y + waterLevel, z).type == Material.AIR ||
                                    chunk.getBlock(x, y + waterLevel, z).type == Material.OAK_LEAVES
                                )
                                    break@y

                                val block = chunk.getBlock(x, y + waterLevel, z)
                                if (block.type == Material.OAK_LOG) {
                                    var localY = 0
                                    do {
                                        localY++
                                    } while (chunk.getBlock(x, y + waterLevel - localY, z).type == Material.OAK_LOG)

                                    trees.add(
                                        IslandTree(
                                            IslandTreeLocation(
                                                (location.x + chunkX) * 16 + x,
                                                waterLevel + y - (localY - 1),
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

                if (!sent25) {
                    if (chunkX.toDouble() / islandSize >= 0.25) {
                        sent25 = true
                        getOwnerPlayer()?.sendMessage("§6Loading: ${"%.2f".format((chunkX.toDouble() / islandSize) * 100)}%")
                    }
                }

                if (!sent50) {
                    if (chunkX.toDouble() / islandSize >= 0.50) {
                        sent50 = true
                        getOwnerPlayer()?.sendMessage("§6Loading: ${"%.2f".format((chunkX.toDouble() / islandSize) * 100)}%")
                    }
                }

                if (!sent75) {
                    if (chunkX.toDouble() / islandSize >= 0.75) {
                        sent75 = true
                        getOwnerPlayer()?.sendMessage("§6Loading: ${"%.2f".format((chunkX.toDouble() / islandSize) * 100)}%")
                    }
                }
            }
            Bukkit.getPlayer(owner)!!.sendMessage("done ${(System.currentTimeMillis() - time) / 1000.0}")
            AnimalCrossing.instance.debugManager.addTreeLogEvent(
                2,
                (System.currentTimeMillis() - time) / 1000.0
            )
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

    fun onJoin(player: Player) {

    }

    fun getCenter(): PlayerLocation {
        val islandCenterX = (islandSize / 2.0) + location.x * islandSize
        val islandCenterZ = (islandSize / 2.0) + location.z * islandSize
        return PlayerLocation(islandCenterX * 16, 0.0, islandCenterZ * 16)
    }

    private fun getOwnerPlayer(): Player? {
        return Bukkit.getPlayer(owner)
    }
}