package dev.reeve.animalcrossing

import org.bukkit.Location

class PlayerLocation(val x: Double, val y: Double, val z: Double) {
    companion object {
        fun fromLocation(location: Location) : PlayerLocation{
            return PlayerLocation(location.x, location.y, location.z)
        }
    }

    override fun toString(): String {
        return "PlayerLocation(x=$x, y=$y, z=$z)"
    }


}