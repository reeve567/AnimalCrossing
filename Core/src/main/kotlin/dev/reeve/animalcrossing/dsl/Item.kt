package dev.reeve.animalcrossing.dsl

import org.bukkit.Material
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

fun item(material: Material, block: ItemStack.() -> Unit): ItemStack = ItemStack(material).apply(block)

fun item(item: ItemStack, block: ItemStack.() -> Unit): ItemStack = item.apply(block)

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