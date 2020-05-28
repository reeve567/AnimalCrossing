package dev.reeve.animalcrossing.island.trees

import com.okkero.skedule.schedule
import dev.reeve.animalcrossing.AnimalCrossing
import dev.reeve.animalcrossing.TickToTime
import dev.reeve.animalcrossing.dsl.item
import dev.reeve.animalcrossing.dsl.itemMeta
import dev.reeve.animalcrossing.utility.up
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.inventory.ItemStack
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random
import kotlin.random.nextInt

class IslandTree(
    private val treeBase: IslandTreeLocation,
    private val fruitType: FruitType,
    private var lastGeneratedFruit: Long = 0,
    private var lastGeneratedSticks: Long = 0,
    private var sticksDropped: Int = 0
) {
    private val maxFruit = 3
    private val maxSticks = 16
    private var lowestLeaves = 255

    @Transient
    private var currentFruit: ArrayList<Location> = ArrayList()

    @Transient
    private var lastUsed = 0L


    init {
        generateFruit()
        generateSticks()
        loadFruit()
    }

    private fun getBlocks(material: Material): ArrayList<Location> {
        val blocks = ArrayList<Location>()
        Bukkit.getScheduler().schedule(AnimalCrossing.instance) {
            var notDone = true
            var y = 0
            while (notDone) {
                y++
                if (treeBase.block.getRelative(0, y, 0).type == Material.AIR) {
                    notDone = false
                }
                for (x in -2..2) {
                    for (z in -2..2) {
                        if ((x == -2 || x == 2) || (z == -2 || z == 2)) {
                            val block = treeBase.block.getRelative(x, y, z)
                            if (block.type == material) {
                                blocks.add(block.location)
                            }
                        }
                    }
                }
            }
        }
        return blocks
    }

    private fun loadFruit() {
        if (currentFruit == null)
            currentFruit = ArrayList()

        if (currentFruit.isEmpty())
            currentFruit.addAll(getBlocks(fruitType.material))
    }

    fun generateFruit() {
        if (Date().time - lastGeneratedFruit >= TickToTime.getTicks(3, 0, 0, 0)) {
            if (currentFruit.size < maxFruit) {
                val leaves = getBlocks(Material.OAK_LEAVES)

                if (lowestLeaves == 255) {
                    leaves.forEach {
                        if (it.blockY < lowestLeaves) {
                            lowestLeaves = it.blockY
                        }
                    }
                }

                while (currentFruit.size < maxFruit) {
                    val random = leaves.random()
                    random.block.type = fruitType.material
                    currentFruit.add(random)
                    leaves.remove(random)
                }

                lastGeneratedFruit = Date().time
            }
        }
    }

    fun generateSticks() {
        if (Date().time - lastGeneratedSticks >= TickToTime.getTicks(1, 0, 0, 0)) {
            sticksDropped = 0
        }
    }

    private fun replaceFruit(index: Int) {
        currentFruit[index].block.type = Material.OAK_LEAVES
        currentFruit.removeAt(index)
    }

    fun shakeTree() {
        if (Date().time - lastUsed < TickToTime.getTicks(0, 0, 0, 3)) {
            return
        }

        fun dropLocation(): Location {
            var x: Int
            var z: Int
            do {
                x = Random.nextInt(-2..2)
                z = Random.nextInt(-2..2)
            } while (x == 0 && z == 0)
            return Location(
                treeBase.block.world,
                treeBase.x + x.toDouble(),
                lowestLeaves - 1.0,
                treeBase.z + z.toDouble()
            )
        }

        when (Random.nextInt(0, 2)) {
            0 -> {
                //fruit
                if (currentFruit == null) loadFruit()

                if (currentFruit.isNotEmpty()) {
                    replaceFruit(Random.nextInt(currentFruit.size))

                    treeBase.block.world.dropItemNaturally(dropLocation(), item(fruitType.material) {
                        itemMeta {
                            setDisplayName(fruitType.label)
                        }
                    })
                } else generateFruit()
            }
            1 -> {
                //stick
                if (sticksDropped < maxSticks)
                    treeBase.block.world.dropItemNaturally(dropLocation(), ItemStack(Material.STICK))
                else generateSticks()
            }
        }
        treeBase.block.world.playSound(treeBase.block.location.up().up(), Sound.BLOCK_GRASS_PLACE, 1.0f, .75f)
        lastUsed = Date().time
    }

    fun isTree(location: Location): Boolean {
        return location.blockX == treeBase.x && location.blockZ == treeBase.z
    }
}