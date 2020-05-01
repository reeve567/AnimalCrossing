package dev.reeve.animalcrossing.extensions

import org.bukkit.Location

fun Location.up(): Location {
	return clone().add(0.0, 1.0, 0.0)
}

fun Location.down(): Location {
	return clone().add(0.0, -1.0, 0.0)
}