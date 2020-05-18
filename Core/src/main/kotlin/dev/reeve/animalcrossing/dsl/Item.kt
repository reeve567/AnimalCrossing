package dev.reeve.animalcrossing.dsl

import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.SkullMeta
import java.util.*

fun item(material: Material, block: ItemStack.() -> Unit): ItemStack = ItemStack(material).apply(block)

fun item(item: ItemStack, block: ItemStack.() -> Unit): ItemStack = item.apply(block)

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