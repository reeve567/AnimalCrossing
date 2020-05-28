package dev.reeve.animalcrossing.island

class IslandLocation(val x: Int, val z: Int) {
	override fun equals(other: Any?): Boolean {
		return if (other is IslandLocation) {
			other.x == x && other.z == z
		} else false
	}
	
	override fun hashCode(): Int {
		var result = x
		result = 31 * result + z
		return result
	}

	override fun toString(): String {
		return "IslandLocation(x=$x, z=$z)"
	}
}