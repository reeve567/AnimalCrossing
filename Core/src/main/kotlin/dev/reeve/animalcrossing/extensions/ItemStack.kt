package dev.reeve.animalcrossing.extensions

import dev.reeve.animalcrossing.Tools
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.tags.ItemTagType

fun ItemStack?.isShovel() : Boolean {
	if (this != null && hasItemMeta()) {
		if (itemMeta!!.customTagContainer.hasCustomTag(Tools.toolKey, ItemTagType.STRING))
			return itemMeta!!.customTagContainer.getCustomTag(Tools.toolKey, ItemTagType.STRING) == "shovel"
	}
	return false
}

fun ItemStack?.isWallet() : Boolean {
	if (this != null && hasItemMeta() && itemMeta!!.customTagContainer.hasCustomTag(Tools.toolKey, ItemTagType.STRING)) {
		return itemMeta!!.customTagContainer.getCustomTag(Tools.toolKey, ItemTagType.STRING) == "wallet"
	}
	return false
}