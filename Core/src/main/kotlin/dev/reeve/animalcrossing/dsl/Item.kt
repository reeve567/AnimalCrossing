package dev.reeve.animalcrossing.dsl

import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import dev.reeve.animalcrossing.AnimalCrossing
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.SkullMeta
import java.util.*
import kotlin.collections.HashMap

val clickableItemListeners = HashMap<UUID, Listener>()

fun item(material: Material, block: ItemStack.() -> Unit): ItemStack = ItemStack(material).apply(block)

fun item(item: ItemStack, block: ItemStack.() -> Unit): ItemStack = item.apply(block)

fun clickableItem(item: ItemStack, player: Player, block: PlayerInteractEvent.() -> Unit): ItemStack {
    if (!clickableItemListeners.contains(player.uniqueId)) {
        clickableItemListeners[player.uniqueId] = object : Listener {
            init {
                Bukkit.getPluginManager().registerEvents(this, AnimalCrossing.instance)
            }

            @EventHandler
            fun onInteract(e: PlayerInteractEvent) {
                if (e.hasItem() && e.item!!.hashCode() == item.hashCode() && e.player.uniqueId == player.uniqueId) {
                    block.invoke(e)
                    e.isCancelled = true
                }
            }

            @EventHandler
            fun onDrop(e: PlayerDropItemEvent) {
                if (e.itemDrop.itemStack.hashCode() == item.hashCode() && e.player.uniqueId == player.uniqueId) {
                    e.isCancelled = true
                }
            }

            @EventHandler
            fun onClick(e: InventoryClickEvent) {
                if (e.currentItem != null && e.currentItem.hashCode() == item.hashCode() && e.whoClicked.uniqueId == player.uniqueId) {
                    e.isCancelled = true
                }
            }
        }
    }
    return item
}

fun skull(value: String): ItemStack {
    val item = ItemStack(Material.PLAYER_HEAD)
    val profile = GameProfile(UUID.randomUUID(), null)
    item.itemMeta {
        this as SkullMeta
        profile.properties.put("textures", Property("textures", value))
        val method = javaClass.getDeclaredMethod("setProfile", GameProfile::class.java)
        method.isAccessible = true
        method.invoke(this, profile)
    }
    return item
}

fun itemNms(
    material: Material,
    block: net.minecraft.server.v1_15_R1.ItemStack.() -> Unit
): net.minecraft.server.v1_15_R1.ItemStack = CraftItemStack.asNMSCopy(ItemStack(material)).apply(block)

fun itemNmsBukkit(material: Material, block: net.minecraft.server.v1_15_R1.ItemStack.() -> Unit): ItemStack =
    CraftItemStack.asBukkitCopy(itemNms(material, block))

fun ItemStack.itemMeta(block: ItemMeta.() -> Unit): ItemMeta {
    val new = itemMeta!!.apply(block)
    itemMeta = new
    return itemMeta!!
}

fun ItemStack.nms(block: net.minecraft.server.v1_15_R1.ItemStack.() -> Unit): ItemStack {
    val stack = CraftItemStack.asNMSCopy(this)
    stack.apply(block)
    return CraftItemStack.asBukkitCopy(stack)
}