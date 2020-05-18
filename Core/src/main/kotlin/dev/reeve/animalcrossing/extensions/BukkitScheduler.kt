package dev.reeve.animalcrossing.extensions

import com.okkero.skedule.BukkitSchedulerController
import com.okkero.skedule.schedule
import dev.reeve.animalcrossing.AnimalCrossing
import org.bukkit.scheduler.BukkitScheduler

fun BukkitScheduler.repeat(delay: Long, condition: () -> Boolean, block: suspend BukkitSchedulerController.() -> Unit) {
    schedule(AnimalCrossing.instance) {
        while (condition.invoke()) {
            block()
            waitFor(delay)
        }
    }
}