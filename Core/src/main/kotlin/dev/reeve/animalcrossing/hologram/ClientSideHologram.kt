package dev.reeve.animalcrossing.hologram

import com.okkero.skedule.schedule
import dev.reeve.animalcrossing.AnimalCrossing
import dev.reeve.animalcrossing.MainMenuManager
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
    val location: Location,
    val hologramType: MainMenuManager.MainMenuHolograms,
    helmet: ItemStack? = null,
    val block: Player.() -> Unit
) :
    Hologram(text, location, helmet, false, false) {
    var looking = false
        set(value) {
            field = value
            update()
        }
    private var moving = false
    private var up = false

    private fun update() {
        Bukkit.getScheduler().schedule(AnimalCrossing.instance) {
            if (looking && !moving && !up) {
                moving = true
                moveTo(location.clone().add(0.0, 0.25, 0.0))
                moving = false
                up = true
            } else if (!looking && !moving && up) {
                moving = true
                moveTo(location)
                moving = false
                up = false
            }
        }
    }

    init {
        deleteRest()
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
}