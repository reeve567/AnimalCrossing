package dev.reeve.animalcrossing.extensions

import dev.reeve.animalcrossing.AnimalCrossing
import dev.reeve.animalcrossing.PlayerLocation
import dev.reeve.animalcrossing.Tools
import dev.reeve.animalcrossing.dsl.clickableItem
import dev.reeve.animalcrossing.dsl.item
import dev.reeve.animalcrossing.dsl.itemMeta
import net.minecraft.server.v1_15_R1.PacketPlayOutWorldBorder
import net.minecraft.server.v1_15_R1.WorldBorder
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.tags.ItemTagType
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

fun Player.getWalletBalance(): Int {
    val wallet = inventory.getItem(Tools.walletSlot)
    if (hasWallet())
        return wallet!!.itemMeta!!.customTagContainer.getCustomTag(Tools.walletKey, ItemTagType.INTEGER)!!
    else setWallet()
    return 0
}

fun Player.hasWallet(): Boolean {
    return inventory.getItem(Tools.walletSlot).isWallet()
}

fun Player.setWallet() {
    inventory.setItem(Tools.walletSlot, Tools.wallet)
}

fun Player.isLookingAtArmorStand(entity: LivingEntity): Boolean {
    return world.rayTraceEntities(eyeLocation, eyeLocation.direction, 4.00) { it is ArmorStand }?.hitEntity.let {
        it != null && it.uniqueId == entity.uniqueId
    }
}

fun Player.setSearchMode() {
    if (isInMainMenuMode()) {
        removeMetadata("mainMenu", AnimalCrossing.instance)
    }
    addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, Int.MAX_VALUE, 1, false, false, false))

    allowFlight = true
    setMetadata("searchMode", FixedMetadataValue(AnimalCrossing.instance, true))
    inventory.clear()
    inventory.setItem(7, clickableItem(item(Material.LIME_WOOL) {
        itemMeta {
            setDisplayName("§aAccept")
        }
    }, this) {
        AnimalCrossing.instance.islandManager.claimIsland(this@setSearchMode, player.location.chunk)
    })
    inventory.setItem(8, clickableItem(item(Material.RED_WOOL) {
        itemMeta {
            setDisplayName("§cNext")
        }
    }, this) {
        AnimalCrossing.instance.islandManager.teleportToUnclaimedIsland(this@setSearchMode)
        setCooldown(Material.RED_WOOL, 100)
    })
}

fun Player.isInSearchMode(): Boolean {
    return hasMetadata("searchMode")
}

fun Player.setNormalMode() {
    if (hasMetadata("mainMenu")) {
        removeMetadata("mainMenu", AnimalCrossing.instance)
    }
    if (hasMetadata("searchMode")) {
        removeMetadata("searchMode", AnimalCrossing.instance)
    }

    allowFlight = false
    removePotionEffect(PotionEffectType.INVISIBILITY)
}

fun Player.setMainMenuMode() {
    setMetadata("mainMenu", FixedMetadataValue(AnimalCrossing.instance, true))
    addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, Int.MAX_VALUE, 1, false, false, false))
}

fun Player.isInMainMenuMode(): Boolean {
    return hasMetadata("mainMenu")
}

fun Player.setWorldBorders(size: Double, location: PlayerLocation) {
    val worldBorder = WorldBorder()
    worldBorder.setCenter(location.x + 0.5, location.z + 0.5)
    worldBorder.size = size
    worldBorder.world = (world as CraftWorld).handle

    val border = PacketPlayOutWorldBorder(worldBorder, PacketPlayOutWorldBorder.EnumWorldBorderAction.INITIALIZE)

    this as CraftPlayer
    handle.playerConnection.sendPacket(border)
}