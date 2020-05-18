package dev.reeve.animalcrossing.adapter

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.bukkit.inventory.ItemStack
import java.lang.reflect.Type

class ItemStackAdapter : JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {
    private val gson: Gson

    override fun deserialize(jsonElement: JsonElement, type: Type, context: JsonDeserializationContext?): ItemStack {
        val map: Map<String, Any> = gson.fromJson(
            jsonElement,
            object : TypeToken<Map<String?, Any?>?>() {}.type
        )
        return ItemStack.deserialize(map)
    }

    override fun serialize(itemStack: ItemStack, type: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(gson.toJson(itemStack.serialize()))
    }

    init {
        gson = GsonBuilder().create()
    }
}