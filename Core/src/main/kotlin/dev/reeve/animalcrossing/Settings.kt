package dev.reeve.animalcrossing

import org.bukkit.Bukkit
import org.bukkit.Location

object Settings {
    val world = Bukkit.getWorld("world")!!
    object MainMenu {
        val location = Location(world, -500.0, 100.0, -500.0, 90f, 0f)
        val pointOne = Location(world, -502.5, 100.0, -499.5, -90f, 0f)
        val pointTwo = Location(world, -501.5, 100.0, -497.5, -145f, 0f)
        val pointThree = Location(world, -501.5, 100.0, -501.5, -45f, 0f)
    }
    object HeadValues {
        val tomNook = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWM4MDczYzE5YzgzMTFhZTdmMjNjM2RkN2U0MTFjNzUxMjUwNzg1ZDY5YmY4MzViMzk2MzM5NzM3MTkxMmEifX19"
        val settings = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTRkNDliYWU5NWM3OTBjM2IxZmY1YjJmMDEwNTJhNzE0ZDYxODU0ODFkNWIxYzg1OTMwYjNmOTlkMjMyMTY3NCJ9fX0="
        val help = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGU0MzU2NTI4MzU1OTJiNDZhMjQxNDhiNGNhNzQyYTRiZGY4ZjY3OGQ3ZDcwYTM4NzkyNzM4Yjg1Y2QzMyJ9fX0="
    }
}