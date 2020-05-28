package dev.reeve.animalcrossing

import com.google.gson.GsonBuilder
import com.mojang.util.UUIDTypeAdapter
import net.minecraft.server.v1_15_R1.Tuple
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer
import org.bukkit.entity.Player
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.URL
import java.util.*
import javax.net.ssl.HttpsURLConnection
import kotlin.collections.ArrayList
import kotlin.collections.HashSet


class TestCommand(private val animalCrossing: AnimalCrossing) : CommandExecutor {
    init {
        Bukkit.getPluginCommand("test")?.setExecutor(this)
    }

    private var start: BlockLocation? = null
    private var end: BlockLocation? = null

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player)
            return false

        if (args.component1() == "one") {
            start = BlockLocation(sender.location.blockX, sender.location.blockY, sender.location.blockZ)
        } else if (args.component1() == "two") {
            end = BlockLocation(sender.location.blockX, sender.location.blockY, sender.location.blockZ)
        } else if (args.component1() == "start") {
            runAStar(sender)
        }
        return true
    }

    private fun getSkin(player: Player, name: String) {

        val connection: HttpsURLConnection = URL(
            String.format(
                "https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false",
                UUIDTypeAdapter.fromUUID(player.uniqueId)
            )
        ).openConnection() as HttpsURLConnection

        if (connection.responseCode == HttpsURLConnection.HTTP_OK) {
            val reply = BufferedReader(InputStreamReader(connection.inputStream)).readLine()
            val skin =
                reply.split("\"value\":\"").toTypedArray()[1].split("\"").toTypedArray()[0]
            val signature =
                reply.split("\"signature\":\"").toTypedArray()[1].split("\"").toTypedArray()[0]
        }

        val file = File(animalCrossing.dataFolder, "$name-skin.json")
        if (!file.exists())
            file.createNewFile()
        player as CraftPlayer

        file.writeText(
            GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(player.profile)
        )
    }

    private fun runAStar(player: Player) {
        val frontier = PriorityQueue<BlockLocation>()
        frontier.add(start!!)

        val visited = HashSet<BlockLocation>()
        visited.add(start!!)

        while (frontier.isNotEmpty()) {
            val current = frontier.remove()

            for (next in current.neighbors()) {
                if (!visited.contains(next)) {
                    frontier.add(next)
                    visited.add(next)
                }
            }
        }
        
    }

    private fun BlockLocation.neighbors(): Array<BlockLocation> {
        val list = ArrayList<BlockLocation>()
        val world = Bukkit.getWorld("world")!!
        val block = getBlock(world)
        for (x in -1..1) {
            for (z in -1..1) {
                val bl = block.getRelative(x, 0, z)
                if (bl.getRelative(0, -1, 0).type.isSolid) {
                    list.add(getRelative(x, 0, z))
                }
            }
        }
        return list.toTypedArray()
    }
}