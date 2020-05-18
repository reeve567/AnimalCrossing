package dev.reeve.animalcrossing

import java.util.*

class PlayerSettings(val uuid: UUID) {
    var music = true
        set(value) {
            field = value
            if (value) {
                AnimalCrossing.instance.musicManager.addPlayer(uuid)
            } else {
                AnimalCrossing.instance.musicManager.removePlayer(uuid)
            }
        }
}