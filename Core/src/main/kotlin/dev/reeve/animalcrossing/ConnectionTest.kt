package dev.reeve.animalcrossing

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.mojang.util.UUIDTypeAdapter
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.*
import javax.net.ssl.HttpsURLConnection


fun main() {
    val connection: HttpsURLConnection = URL(
        "https://sessionserver.mojang.com/session/minecraft/profile/${UUIDTypeAdapter.fromUUID(UUID.fromString("e17b9519-a0de-4913-9e35-9341dec23cc5"))}?unsigned=false"
    ).openConnection() as HttpsURLConnection

    assert(connection.responseCode == HttpsURLConnection.HTTP_OK)

    val element = JsonParser().parse(BufferedReader(InputStreamReader(connection.inputStream)).readLines().joinToString("")) as JsonObject
    println(GsonBuilder().setPrettyPrinting().create().toJson(element))

    println("Value")
    println(element["properties"].asJsonArray[0].asJsonObject["value"].asString)
    println("Signature")
    println(element["properties"].asJsonArray[0].asJsonObject["signature"].asString)
}
