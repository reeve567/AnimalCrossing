package dev.reeve.animalcrossing.dsl

import dev.reeve.animalcrossing.AnimalCrossing
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class GUI(size: Int = 54, val items: ArrayList<ClickableItem> = ArrayList()) : Listener {
	private val inv: Inventory = Bukkit.createInventory(null, size, "Inventory")

	init {
		Bukkit.getPluginManager().registerEvents(this,
			AnimalCrossing.instance
		)
	}

	fun add(clickableItem: ClickableItem) {
		items.add(clickableItem)
		clickableItem.slot?.let { inv.setItem(it, clickableItem.itemStack) }
	}

	fun open(player: Player) {
		player.openInventory(inv)
	}

	@EventHandler
	fun onClose(e: InventoryCloseEvent) {
		if (e.inventory.hashCode() == inv.hashCode()) {
			for (item in items) {
				InventoryClickEvent.getHandlerList().unregister(item)
			}
		}
	}
}

class ClickableItem(var itemStack: ItemStack? = null, var slot: Int? = null, var click: ClickAction? = null) :
	Listener {
	init {
		Bukkit.getPluginManager().registerEvents(this,
			AnimalCrossing.instance
		)
	}

	@EventHandler
	fun onEvent(e: InventoryClickEvent) {
		if (e.currentItem.hashCode() == itemStack!!.hashCode()) {
			click?.action?.let { e.apply(it) }
		}
	}
}

open class ClickAction(val action: InventoryClickEvent.() -> Unit)

class NoClickAction : ClickAction({ isCancelled = true })

class OpenInventoryClickAction(val inventory: Inventory) : ClickAction({
	this.inventory
})

fun gui(size: Int = 54, block: GUI.() -> Unit): GUI = GUI(
	size
).apply(block)

fun GUI.clickableItem(block: ClickableItem.() -> Unit) {
	val clickableItem = ClickableItem()
	clickableItem.apply(block)
	this.add(clickableItem)
}

fun ClickableItem.clickAction(block: InventoryClickEvent.() -> Unit): ClickAction {
	val clickAction = ClickAction(block)
	click = clickAction
	return clickAction
}