package dev.reeve.animalcrossing

import dev.reeve.animalcrossing.dsl.item
import dev.reeve.animalcrossing.dsl.itemMeta
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.tags.ItemTagType

@SuppressWarnings("deprecated")
object Tools {
	val toolKey = NamespacedKey(AnimalCrossing.instance, "tool")
	val walletKey = NamespacedKey(AnimalCrossing.instance, "walletBalance")
	val durabilityKey = NamespacedKey(AnimalCrossing.instance, "durability")

	private fun durabilityLore(durability: Int): String {
		return "§8Durability left: §7$durability"
	}

	private fun ItemMeta.addToolKey(value: String) {
		customTagContainer.setCustomTag(toolKey, ItemTagType.STRING, value)
	}

	private fun ItemMeta.setDurability(int: Int) {
		lore = listOf(durabilityLore(int))
		customTagContainer.setCustomTag(durabilityKey, ItemTagType.INTEGER, int)
	}
	
	private fun ItemMeta.setUnbreakable() {
		isUnbreakable = true
		addItemFlags(ItemFlag.HIDE_UNBREAKABLE)
	}

	val walletSlot = 8
	val wallet
		get() : ItemStack {
			return item(Material.CONDUIT) {
				itemMeta {
					setDisplayName("§fWallet")
					lore = listOf("§60 Bells")
					addToolKey("wallet")
					customTagContainer.setCustomTag(walletKey, ItemTagType.INTEGER, 0)
				}
			}
		}

	object Shovels {
		val shovels = arrayOf(flimsyShovel, shovel, goldenShovel)

		val flimsyShovel
			get() : ItemStack {
				return item(Material.WOODEN_SHOVEL) {
					itemMeta {
						setDisplayName("§fFlimsy Shovel")
						setDurability(40)
						addToolKey("shovel")
						setUnbreakable()
					}
				}
			}
		val shovel
			get() : ItemStack {
				return item(Material.STONE_SHOVEL) {
					itemMeta {
						setDisplayName("§fShovel")
						setDurability(100)
						addToolKey("shovel")
						setUnbreakable()
					}
				}
			}
		val goldenShovel
			get() : ItemStack {
				return item(Material.GOLDEN_SHOVEL) {
					itemMeta {
						setDisplayName("§fGolden Shovel")
						setDurability(200)
						addToolKey("shovel")
						setUnbreakable()
					}
				}
			}
	}
}