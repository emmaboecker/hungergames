package net.stckoverflw.hg.command

import net.axay.kspigot.commands.argument
import net.axay.kspigot.commands.command
import net.axay.kspigot.commands.runs
import net.axay.kspigot.commands.suggestList
import net.axay.kspigot.extensions.onlinePlayers
import net.stckoverflw.hg.HungerGamesPlugin

fun spectateCommand(plugin: HungerGamesPlugin) = command("spectate") {
    runs {
        val player = sender.player ?: return@runs

        plugin.game.addSpectator(player.uuid)
    }
    argument<String>("player") {
        suggestList {
            onlinePlayers.filter { it.uniqueId in plugin.game.players }.map { it.name }
        }
        runs {
            val playerName = this.getArgument<String>("player")

            val player = plugin.server.getPlayer(playerName) ?: return@runs

            plugin.game.addSpectator(player.uniqueId)
        }
    }
}