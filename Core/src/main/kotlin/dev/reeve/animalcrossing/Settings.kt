package dev.reeve.animalcrossing

import org.bukkit.Bukkit
import org.bukkit.Location

object Settings {
    val world = Bukkit.getWorld("world")!!

    object MainMenu {
        val location = Location(world, -500.0, 100.0, -500.0, 90f, 0f)
        val pointOne = Location(world, -502.5, 100.0, -499.5, -90f, 0f)
        val pointTwo = Location(world, -501.5, 100.0, -497.5, -145f, 0f)
        val pointThree = Location(world, -501.5, 100.0, -501.5, -45f, 0f)
    }

    object HeadValues {
        val tomNook =
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWM4MDczYzE5YzgzMTFhZTdmMjNjM2RkN2U0MTFjNzUxMjUwNzg1ZDY5YmY4MzViMzk2MzM5NzM3MTkxMmEifX19"
        val settings =
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTRkNDliYWU5NWM3OTBjM2IxZmY1YjJmMDEwNTJhNzE0ZDYxODU0ODFkNWIxYzg1OTMwYjNmOTlkMjMyMTY3NCJ9fX0="
        val help =
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGU0MzU2NTI4MzU1OTJiNDZhMjQxNDhiNGNhNzQyYTRiZGY4ZjY3OGQ3ZDcwYTM4NzkyNzM4Yjg1Y2QzMyJ9fX0="
    }

    object SkinValues {
        val tomNook = "ewogICJ0aW1lc3RhbXAiIDogMTU4OTk0MjE5MzUyOSwKICAicHJvZmlsZUlkIiA6ICJlM2I0NDVjODQ3ZjU0OGZiOGM4ZmEzZjFmN2VmYmE4ZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJNaW5pRGlnZ2VyVGVzdCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9hYmZjNTYyZjQwZDdmZDA4M2YwNGM0OTI4OTZhYzNkZmUzY2M0NTg1MGQ1NDlmNzA2OTY3NDllMjVlZmE5NjM3IgogICAgfQogIH0KfQ==" to "Zu19nI091kvsfsVNg6HZktQDYmGmlfU9iB97ohHz+1mY6CPTICru0A5ly0xmyavPshWT9EurzMjz9LWyYQElBPpSBueK8ZF+d2T9rlkpK6OnmQ4bIPIQqIG6dOg3lUoT12gpOHK3QgYOcfmfcLxoGYGhdWx8N/VPvDnQG+yTVvkU9pV36NvQYqt056PIYHffgcjVxmks6TaIC0rDRL98/ZyUgWkwG2MQBV33pfG1Day/NIqQ+XgDlpvoYitkAqIZhby/Ah88D2H1PEFhqG8KrR1kfPj66Qvz/6q3fpYPL90YI2Bgi7Q322RRgCvywaUFr5dUygXMDBbkaf5QJbd8E8KEJyJUCMSDsN4cLkF5tu1kYIDxMo8WvdSFhnRXi5mu4pWDTX8J+btdTUdcb149tMNpafHYkntUIEZle5eNWaAwIuvzwV/3XNCCCfqyvjQWRPJqyrZ2YbFjxzwdw9jwp6BahxeaandLMaR8gwTJ1khNk6LDNW1b54lkcf646Q5O+D6grqgWAcNEs5nBwKUCZpDXo2W0LsSuT2/zwOWwqnVMuVatMuGfnxxA+jREs3UOa16Grf7Rc38CxfJIdZh49s0yJLv4zUl2HQO1iCycbtz+JZfUU40EzL0gC/TWKyS97TFntB7/S2dptfPNE2gHb3pi+QZHWuRfBWIgUgTdIzc="
    }
}