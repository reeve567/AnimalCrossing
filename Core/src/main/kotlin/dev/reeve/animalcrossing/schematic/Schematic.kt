package dev.reeve.animalcrossing.schematic

import dev.reeve.animalcrossing.BlockLocation
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.BlockFace
import org.bukkit.block.data.BlockData
import org.bukkit.block.data.Directional
import kotlin.math.abs
import kotlin.math.min

class Schematic(val name: String) : Cloneable {
    private var width: Int = 0
    private var height: Int = 0
    private var length: Int = 0
    private var center = BlockLocation(0, 0, 0)
    private var blocks = ArrayList<String>()

    @Transient
    var blocksInitialized: Array<Array<Array<BlockData?>>>? = null

    private fun load() {
        if (blocksInitialized == null)
            blocksInitialized =
                Array(width) { x ->
                    Array(length) { z ->
                        val column = blocks[x * width + z].split("|")
                        Array(height) { y ->
                            try {
                                Bukkit.getServer().createBlockData(column[y])
                            } catch (e: IllegalArgumentException) {
                                null
                            }
                        }
                    }
                }
    }

    fun place(world: World, x: Int, y: Int, z: Int) {
        load()
        val base = Location(world, x - center.x.toDouble(), y - center.y.toDouble(), z - center.z.toDouble())
        for (x in 0 until width) {
            for (z in 0 until length) {
                for (y in 0 until height) {
                    if (blocksInitialized!![x][z][y]!!.material != Material.AIR)
                        base.block.getRelative(x, y, z).blockData = blocksInitialized!![x][z][y]!!
                }
            }
        }
    }

    fun place(location: Location) {
        place(location.world!!, location.blockX, location.blockY, location.blockZ)
    }

    fun rotate(rotation: Rotation): Schematic {
        val schem = Schematic(name)
        schem.height = height

        schem.load()
        when (rotation) {
            Rotation.NINETY_DEGREES -> {
                schem.width = length
                schem.length = width
                schem.blocksInitialized =
                    Array(length) { x ->
                        Array(width) { z ->
                            schem.blocksInitialized!![z][(length - 1) - x].map {
                                it?.also {
                                    if (it is Directional) {
                                        it.rotate(1)
                                    }
                                }
                            }.toTypedArray()
                        }
                    }
            }
            Rotation.ONE_HUNDRED_AND_EIGHTY_DEGREES -> {
                schem.width = width
                schem.length = length
                schem.blocksInitialized =
                    Array(width) { x ->
                        Array(length) { z ->
                            schem.blocksInitialized!![(width - 1) - x][(length - 1) - z].map {
                                it?.also {
                                    if (it is Directional) {
                                        it.rotate(2)
                                    }
                                }
                            }.toTypedArray()
                        }
                    }
            }
            Rotation.TWO_HUNDRED_AND_SEVENTY_DEGREES -> {
                schem.width = length
                schem.length = width
                schem.blocksInitialized =
                    Array(length) { x ->
                        Array(width) { z ->
                            schem.blocksInitialized!![(width - 1) - z][x].map {
                                it?.also {
                                    if (it is Directional) {
                                        it.rotate(3)
                                    }
                                }
                            }.toTypedArray()
                        }
                    }
            }
        }
        return schem
    }

    fun setBlocks(positionOne: Location, positionTwo: Location, center: Location) {
        width = abs(positionOne.blockX - positionTwo.blockX) + 1
        height = abs(positionOne.blockY - positionTwo.blockY) + 1
        length = abs(positionOne.blockZ - positionTwo.blockZ) + 1

        val base = Location(
            positionOne.world,
            min(positionOne.x, positionTwo.x),
            min(positionOne.y, positionTwo.y),
            min(positionOne.z, positionTwo.z)
        )

        this.center.x = abs(base.blockX - center.blockX)
        this.center.z = abs(base.blockZ - center.blockZ)
        this.center.y = abs(base.blockY - center.blockY)

        for (x in 0 until width) {
            for (z in 0 until length) {
                var string = ""
                for (y in 0 until height) {
                    val block = base.block.getRelative(x, y, z)
                    string += block.blockData.asString
                    string += "|"
                }
                blocks.add(string.substring(0, string.length - 1))
            }
        }
    }

    enum class Rotation {
        NINETY_DEGREES,
        ONE_HUNDRED_AND_EIGHTY_DEGREES,
        TWO_HUNDRED_AND_SEVENTY_DEGREES
    }

    private fun Directional.rotate(amount: Int) {
        for (i in 0 until amount) {
            when (facing) {
                BlockFace.NORTH -> facing = BlockFace.EAST
                BlockFace.EAST -> facing = BlockFace.SOUTH
                BlockFace.SOUTH -> facing = BlockFace.WEST
                BlockFace.WEST -> facing = BlockFace.NORTH
                else -> {
                }
            }
        }
    }

}