package dev.reeve.animalcrossing.extensions

import dev.reeve.animalcrossing.Tools
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.tags.ItemTagType

fun Player.getWalletBalance() : Int {
	val wallet = inventory.getItem(Tools.walletSlot)
	if (hasWallet())
		return wallet!!.itemMeta!!.customTagContainer.getCustomTag(Tools.walletKey, ItemTagType.INTEGER)!!
	else setWallet()
	return 0;
}

fun Player.hasWallet() : Boolean {
	return inventory.getItem(Tools.walletSlot).isWallet()
}

fun Player.setWallet() {
	inventory.setItem(Tools.walletSlot, Tools.wallet)
}