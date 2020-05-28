package dev.reeve.animalcrossing.island.npc

import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import net.minecraft.server.v1_15_R1.*
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_15_R1.CraftServer
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer
import org.bukkit.entity.Player
import java.util.*

class NPC(val name: String, val location: Location, skin: Pair<String, String>) {
    @Transient
    val entityPlayer: EntityPlayer

    init {
        val server = ((Bukkit.getServer()) as CraftServer).server
        val world = (Bukkit.getWorld("world") as CraftWorld).handle
        val profile = GameProfile(UUID.randomUUID(), "§6$name")
        profile.properties.put("textures", Property("textures", skin.first, skin.second))
        val playerInteractManager = PlayerInteractManager(world)
        entityPlayer = EntityPlayer(server, world, profile, playerInteractManager)
        entityPlayer.setLocation(location.x, location.y, location.z, location.yaw, location.pitch)
        entityPlayer.dataWatcher.set(DataWatcherObject(12, DataWatcherRegistry.a), 0xFF.toByte())
    }

    fun sendPacket(player: Player) {
        player as CraftPlayer
        player.handle.playerConnection.sendPacket(
            PacketPlayOutPlayerInfo(
                PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER,
                entityPlayer
            )
        )
        player.handle.playerConnection.sendPacket(PacketPlayOutNamedEntitySpawn(entityPlayer))
        player.handle.playerConnection.sendPacket(
            PacketPlayOutEntityHeadRotation(
                entityPlayer,
                (entityPlayer.yaw * 256 / 360).toByte()
            )
        )

    }

    class NPCEntity(
        minecraftServer: MinecraftServer?,
        worldServer: WorldServer?,
        gameProfile: GameProfile?,
        playerInteractManager: PlayerInteractManager?
    ) : EntityPlayer(minecraftServer, worldServer, gameProfile, playerInteractManager) {
        init {

        }
    }
}