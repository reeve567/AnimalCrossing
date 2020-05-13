package dev.reeve.animalcrossing.adapters

import com.google.gson.*
import dev.reeve.animalcrossing.island.Island
import dev.reeve.animalcrossing.island.IslandLocation
import java.lang.reflect.Type
import java.util.*

class IslandAdapter : JsonDeserializer<Island>, JsonSerializer<Island> {
    override fun deserialize(json: JsonElement?, p1: Type?, p2: JsonDeserializationContext?): Island {
        return Island(IslandLocation(0,0), UUID.randomUUID())
    }

    override fun serialize(island: Island?, p1: Type?, p2: JsonSerializationContext?): JsonElement {
        if (island != null) {
            return JsonObject()
        } else return JsonObject()
    }
}