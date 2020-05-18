package dev.reeve.animalcrossing.music

import com.okkero.skedule.schedule
import org.bukkit.Bukkit
import org.bukkit.Note
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.InputStreamReader
import java.util.*
import kotlin.collections.ArrayList

class Song(private val plugin: JavaPlugin, name: String) {
    private val body: JSONObject =
        JSONParser().parse(InputStreamReader(plugin.getResource("songs/$name.json")!!)) as JSONObject
    private val players = ArrayList<UUID>()

    fun play() {
        Bukkit.getScheduler().schedule(plugin) {
            val array = body["content"] as JSONArray
            for (obj in array) {
                val strings = obj.toString().split(";")

                if (strings[0].contains("d")) {
                    val wait = strings[0].substring(1).toLong()
                    waitFor(wait * 2)
                } else {
                    var note: Note
                    var sound: Sound
                    if (strings[0].contains("i")) {
                        note = Note(strings[1].substring(1).toInt())
                        sound = when (strings[0].substring(1).toInt()) {
                            0 -> Sound.BLOCK_NOTE_BLOCK_HARP
                            1 -> Sound.BLOCK_NOTE_BLOCK_BASS
                            else -> Sound.BLOCK_NOTE_BLOCK_HARP
                        }
                    } else {
                        note = Note(strings[0].substring(1).toInt())
                        sound = Sound.BLOCK_NOTE_BLOCK_HARP
                    }
                    players.forEach { uuid ->
                        val player = Bukkit.getPlayer(uuid)
                        player?.playSound(
                            player.eyeLocation,
                            sound,
                            SoundCategory.RECORDS,
                            1.0f,
                            NoteUtils.getPitch(note)
                        )
                    }
                }

            }
        }
    }

    fun addPlayer(player: Player) {
        players.add(player.uniqueId)
    }

    fun addPlayer(uuid: UUID) {
        players.add(uuid)
    }

    fun removePlayer(uuid: UUID) {
        players.remove(uuid)
    }
}