package dev.reeve.animalcrossing.music

import org.bukkit.Note
import kotlin.math.pow

object NoteUtils {
    private val pitchValues = Array(25) {
        2.0.pow((-12 + it) / 12.0).toFloat()
    }

    fun getPitch(note: Note): Float {
        return pitchValues[note.id.toInt()]
    }
}