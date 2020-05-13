package dev.reeve.animalcrossing.island.trees

import org.bukkit.Material

enum class FruitType(val material: Material) {
    PEACH(Material.PINK_WOOL),
    APPLE(Material.RED_WOOL),
    PEAR(Material.LIME_WOOL),
    ORANGE(Material.ORANGE_WOOL)
    ;
    val label = "§6${name.substring(0, 1).toUpperCase()}${name.substring(1).toLowerCase()}"
}