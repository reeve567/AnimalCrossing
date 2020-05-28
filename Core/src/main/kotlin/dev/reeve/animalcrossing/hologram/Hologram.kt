package dev.reeve.animalcrossing.hologram

import com.okkero.skedule.BukkitSchedulerController
import org.bukkit.Location
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.inventory.ItemStack
import java.util.*

open class Hologram(text: String, location: Location, helmet: ItemStack? = null, marker: Boolean = true, val register: Boolean = true) : Listener {
    protected val armorStand: ArmorStand
    val uuid: UUID

    init {
        if (!location.chunk.isForceLoaded) {
            location.chunk.load()
            location.chunk.isForceLoaded = true
        }

        armorStand = location.world!!.spawnEntity(location, EntityType.ARMOR_STAND) as ArmorStand
        uuid = armorStand.uniqueId

        if (marker)
            armorStand.isMarker = true
        armorStand.isVisible = false
        armorStand.isPersistent = false
        armorStand.setBasePlate(false)
        armorStand.isCustomNameVisible = true
        armorStand.customName = text
        armorStand.setGravity(false)

        helmet?.also {
            armorStand.setHelmet(helmet)
        }

        armorStand.isInvulnerable = true
        armorStand.removeWhenFarAway = false
    }

    open fun destroy() {
        if (!armorStand.location.chunk.isLoaded)
            armorStand.location.chunk.load()
        armorStand.remove()
        PlayerInteractAtEntityEvent.getHandlerList().unregister(this)
    }

    suspend fun BukkitSchedulerController.moveTo(location: Location) {
        val originalLocation = armorStand.location.clone()

        val movements = 5

        val incrementX = (location.x - originalLocation.x) / movements
        val incrementY = (location.y - originalLocation.y) / movements
        val incrementZ = (location.z - originalLocation.z) / movements

        for (i in 0 until movements) {
            armorStand.teleport(originalLocation.add(incrementX, incrementY, incrementZ))
            waitFor(1)
        }
    }

    @EventHandler
    open fun onClick(e: PlayerInteractAtEntityEvent) {
        if (e.rightClicked.uniqueId == uuid) e.isCancelled = true
    }
}