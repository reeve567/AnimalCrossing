package dev.reeve.animalcrossing.generator

import dev.reeve.animalcrossing.utility.up
import org.bukkit.Chunk
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.generator.BlockPopulator
import java.util.*
import kotlin.random.nextInt

class TreePopulator : BlockPopulator() {
    private val treesPerChunk = 1
    private val maxTries = 5

    override fun populate(world: World, random: Random, source: Chunk) {
        if (random.nextInt(2) == 1) {
            for (i in 0 until treesPerChunk) {
                var tries = 0
                while (tries < maxTries) {
                    tries++
                    val block = world.getHighestBlockAt(
                        source.x * 16 + Random().nextInt(10) + 3,
                        source.z * 16 + Random().nextInt(10) + 3
                    )
                    if (!block.isLiquid && block.type == Material.GRASS_BLOCK) {
                        var bottom = block.location
                        for (y in kotlin.random.Random.nextInt(1..2)..4) {
                            bottom = bottom.up()
                            bottom.block.setType(Material.OAK_LOG, false)
                        }

                        for (x in -1..1) {
                            for (z in -1..1) {
                                bottom.block.getRelative(x, 0, z).setType(Material.OAK_LEAVES, false)
                            }
                        }
                        bottom.block.getRelative(0,0,0).setType(Material.OAK_LOG, false)

                        for (x in -2..2) {
                            for (z in -2..2) {
                                if (!(x == -2 && z == -2) && !(x == 2 && z == 2) && !(x == 2 && z == -2) && !(x == -2 && z == 2)) {
                                    bottom.block.getRelative(x, 1, z).setType(Material.OAK_LEAVES, false)
                                }
                            }
                        }
                        bottom.block.getRelative(0,1,0).setType(Material.OAK_LOG, false)

                        for (x in -1..1) {
                            for (z in -1..1) {
                                bottom.block.getRelative(x, 2, z).setType(Material.OAK_LEAVES, false)
                            }
                        }

                        bottom.block.getRelative(-2, 2, 0).setType(Material.OAK_LEAVES, false)
                        bottom.block.getRelative(2, 2, 0).setType(Material.OAK_LEAVES, false)
                        bottom.block.getRelative(0, 2, -2).setType(Material.OAK_LEAVES, false)
                        bottom.block.getRelative(0, 2, 2).setType(Material.OAK_LEAVES, false)

                        bottom.block.getRelative(0, 3, 1).setType(Material.OAK_LEAVES, false)
                        bottom.block.getRelative(0, 3, -1).setType(Material.OAK_LEAVES, false)
                        bottom.block.getRelative(1, 3, 0).setType(Material.OAK_LEAVES, false)
                        bottom.block.getRelative(-1, 3, 0).setType(Material.OAK_LEAVES, false)
                        bottom.block.getRelative(0, 3, 0).setType(Material.OAK_LEAVES, false)

                        break
                    }
                }
            }
        }

    }

}