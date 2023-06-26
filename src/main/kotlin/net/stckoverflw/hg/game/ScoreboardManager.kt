package net.stckoverflw.hg.game

import net.axay.kspigot.event.listen
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.scoreboard.Criteria
import org.bukkit.scoreboard.DisplaySlot

class ScoreboardManager(val game: HungerGamesGame) {

    init {
        listen<PlayerJoinEvent>(priority = EventPriority.HIGH) {
            val scoreboard = it.player.scoreboard

            val objective = scoreboard.registerNewObjective(
                it.player.uniqueId.toString(),
                Criteria.DUMMY,
                game.miniMessage.deserialize("<gradient:#f24a4f:#5e83c5><bold>The JacobStreams Games")
            ).apply {
                displaySlot = DisplaySlot.SIDEBAR
            }

            scoreboard.registerNewTeam(it.player.uniqueId.toString() + "time").apply {
                addEntry("§1")
            }

            scoreboard.registerNewTeam(it.player.uniqueId.toString() + "border").apply {
                addEntry("§2")
            }

            scoreboard.registerNewTeam(it.player.uniqueId.toString() + "alive").apply {
                addEntry("§4")
            }

            scoreboard.registerNewTeam(it.player.uniqueId.toString() + "kills").apply {
                addEntry("§5")
            }

            scoreboard.registerNewTeam(it.player.uniqueId.toString() + "placeholder1").apply {
                addEntry("§7")
                prefix(game.miniMessage.deserialize("<#f24a4f>jacobstreams.com"))
            }

            scoreboard.registerNewTeam(it.player.uniqueId.toString() + "placeholder2").apply {
                addEntry("§8")
                prefix(game.miniMessage.deserialize("<#5e83c5>www.stckoverflw.net"))
            }

            objective.getScore("§0").score = 9
            objective.getScore("§1").score = 8
            objective.getScore("§2").score = 7
            objective.getScore("§3").score = 6
            objective.getScore("§4").score = 5
            objective.getScore("§5").score = 4
            objective.getScore("§6").score = 3
            objective.getScore("§7").score = 2
            objective.getScore("§8").score = 1

            reloadAlivePlayers(it.player)
            reloadKills(it.player)

            it.player.scoreboard = scoreboard
        }
    }

    fun reloadAlivePlayers(player: Player) {
        val scoreboard = player.scoreboard

        scoreboard.getTeam(player.uniqueId.toString() + "alive")?.let { team ->
            team.prefix(Component.text("Players Alive: ", NamedTextColor.GRAY))
            team.suffix(Component.text(game.players.size, NamedTextColor.BLUE, TextDecoration.BOLD))
        }
    }

    fun reloadKills(player: Player) {
        val scoreboard = player.scoreboard

        scoreboard.getTeam(player.uniqueId.toString() + "kills")?.let { team ->
            team.prefix(Component.text("Your Kills: ", NamedTextColor.GRAY))
            team.suffix(Component.text(game.stats.getKills(player).size, NamedTextColor.RED, TextDecoration.BOLD))
        }
    }

    fun setTime(player: Player, timeFor: Component, time: Component) {
        val scoreboard = player.scoreboard

        scoreboard.getTeam(player.uniqueId.toString() + "time")?.let { team ->
            team.prefix(timeFor.append(Component.text(": ", NamedTextColor.GRAY)))
            team.suffix(time)
        }
    }

    fun reloadBorder(player: Player) {
        val scoreboard = player.scoreboard

        scoreboard.getTeam(player.uniqueId.toString() + "border")?.let { team ->
            team.prefix(Component.text("Border Size: ", NamedTextColor.GRAY))
            team.suffix(Component.text(game.world.worldBorder.size.toInt(), NamedTextColor.BLUE))
        }
    }

}