package dev.reeve.animalcrossing

object TickToTime {
	private const val second = 1000
	private const val minute = 60 * second
	private const val hour = 60 * minute
	private const val day = 24L * hour
	
	
	fun getTicks(days: Int, hours: Int, minutes: Int, seconds: Int) : Long {
		return seconds * second + minutes * minute + hours * hour + days * day
	}
}