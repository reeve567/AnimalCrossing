package dev.reeve.animalcrossing.hologram

import dev.reeve.animalcrossing.extensions.isLookingAtArmorStand
import dev.reeve.animalcrossing.extensions.repeat
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityDestroy
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.inventory.ItemStack

class ClientSideHologram(
    val player: Player,
    text: String,
    location: Location,
    helmet: ItemStack? = null,
    private val block: PlayerInteractAtEntityEvent.() -> Unit
) :
    Hologram(text, location, helmet, false) {
    private var looking = false
    private var moving = false

    init {
        deleteRest()
        Bukkit.getScheduler().repeat(5, { !armorStand.isDead }) {
            if (!looking && !moving) {
                if (player.isLookingAtArmorStand(armorStand)) {
                    looking = true
                    moving = true
                    moveTo(location.clone().add(0.0, 0.25, 0.0))
                    moving = false
                }
            } else if (!moving) {
                if (!player.isLookingAtArmorStand(armorStand)) {
                    // return back to normal spot
                    looking = false
                    moving = true
                    moveTo(location)
                    moving = false
                }
            }
        }
    }


    private fun deleteRest() {
        for (player in Bukkit.getOnlinePlayers()) {
            deletePlayer(player)
        }
    }

    fun deletePlayer(player: Player) {
        if (player.uniqueId != this.player.uniqueId)
            (player as CraftPlayer).handle.playerConnection.sendPacket(PacketPlayOutEntityDestroy(armorStand.entityId))
    }

    @EventHandler
    override fun onClick(e: PlayerInteractAtEntityEvent) {
        super.onClick(e)
        if (e.rightClicked.entityId == armorStand.entityId)
            e.apply(block)
    }
}