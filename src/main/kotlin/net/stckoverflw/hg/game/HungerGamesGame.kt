package net.stckoverflw.hg.game

import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.onlinePlayers
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.megavex.scoreboardlibrary.api.ScoreboardLibrary
import net.megavex.scoreboardlibrary.api.noop.NoopScoreboardLibrary
import net.stckoverflw.hg.HungerGamesPlugin
import net.stckoverflw.hg.game.state.GameState
import net.stckoverflw.hg.game.state.WaitingState
import org.bukkit.GameMode
import org.bukkit.World
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.scoreboard.Team
import java.util.*


class HungerGamesGame(val plugin: HungerGamesPlugin, val world: World) {

    val miniMessage = MiniMessage.miniMessage()

    val players: ArrayList<UUID> = arrayListOf()

    var gameState: GameState = WaitingState(this)
        set(value) {
            field.internalStop()
            field = value
            value.start()
        }

    val chestFilling = ChestFilling(this)

    private val scoreboardLibrary: ScoreboardLibrary

    val stats = Stats(this)

    init {
        gameState.start()

        scoreboardLibrary = NoopScoreboardLibrary()

        listen<PlayerJoinEvent>(priority = EventPriority.LOW) { event ->
            if (gameState is WaitingState && !players.contains(event.player.uniqueId)) {
                players.add(event.player.uniqueId)
            } else {
                event.joinMessage(null)
                event.player.gameMode = GameMode.SPECTATOR
            }

            event.player.scoreboard = plugin.server.scoreboardManager.newScoreboard

            reloadTeams()
        }

        listen<PlayerDeathEvent> {
            it.deathMessage()
        }
    }

    fun addSpectator(player: UUID) {
        players.remove(player)

        if (plugin.game.gameState is WaitingState) {
            plugin.game.players.forEachIndexed { index, current ->
                plugin.server.getPlayer(current)
                    ?.teleport(plugin.gameConfig.spawnLocations[index % plugin.gameConfig.spawnLocations.size])
            }
        }

        reloadTeams()
    }

    private fun reloadTeams() {
        onlinePlayers.forEach { player ->
            player.scoreboard.teams.forEach { team -> team.unregister() }

            val team = player.scoreboard.registerNewTeam(player.name)
            team.addEntry(player.name)
            team.displayName(Component.text(player.name))
            team.setAllowFriendlyFire(false)
            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER)
            if (player.uniqueId in players) {
                player.playerListName(
                    miniMessage.deserialize(
                        "<transition:red:yellow:green:aqua:blue:dark_purple:light_purple:${
                            players.indexOf(
                                player.uniqueId
                            ).toDouble() / players.size.toDouble()
                        }>${player.name}</transition>"
                    )
                )
            } else {
                player.playerListName(miniMessage.deserialize("<gray>${player.name}</gray>"))
            }

        }
    }

}