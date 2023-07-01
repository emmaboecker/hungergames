package net.stckoverflw.hg.game

import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.broadcast
import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.runnables.sync
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.minimessage.MiniMessage
import net.stckoverflw.hg.HungerGamesPlugin
import net.stckoverflw.hg.game.state.GameState
import net.stckoverflw.hg.game.state.SetupState
import net.stckoverflw.hg.game.state.WaitingState
import net.stckoverflw.hg.game.state.WinState
import org.bukkit.GameMode
import org.bukkit.GameRule
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.Container
import org.bukkit.event.EventPriority
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.scoreboard.Team
import java.util.*


class HungerGamesGame(val plugin: HungerGamesPlugin, val world: World) {

    val miniMessage = MiniMessage.miniMessage()

    val players: ArrayList<UUID> = arrayListOf()

    var gameState: GameState = WaitingState(this)
        set(value) {
            field.internalStop()
            field = value
            sync {
                value.start()
            }
        }

    val chestFilling = ChestFilling(this)

    val stats = Stats(this)

    val scoreboardManager = ScoreboardManager(this)

    var placedBlocks = mutableSetOf<Location>()

    init {
        gameState.start()

        world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false)
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false)
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false)
        world.setGameRule(GameRule.DISABLE_RAIDS, true)
        world.setGameRule(GameRule.DO_MOB_SPAWNING, false)
        world.setGameRule(GameRule.DO_TRADER_SPAWNING, false)

        listen<PlayerJoinEvent>(priority = EventPriority.LOW) { event ->
            event.joinMessage(null)
            if (gameState is WaitingState && !players.contains(event.player.uniqueId)) {
                players.add(event.player.uniqueId)
                val component = miniMessage.deserialize("<blue><bold>${event.player.name}</bold> <white>joined")
                onlinePlayers.forEach {
                    it.sendActionBar(component)
                }
            } else {
                event.player.gameMode = GameMode.SPECTATOR
            }

            event.player.scoreboard = plugin.server.scoreboardManager.newScoreboard

            reloadTeams()
        }

        listen<PlayerQuitEvent> { event ->
            event.quitMessage(null)

            if (event.player.uniqueId in players) {
                val component = miniMessage.deserialize("<red><bold>${event.player.name}</bold> <white>left")
                onlinePlayers.forEach {
                    it.sendActionBar(component)
                }
            }
        }

        listen<PlayerDeathEvent> {
            it.isCancelled = true
            it.player.gameMode = GameMode.SPECTATOR
            it.drops.forEach { item ->
                it.player.world.dropItem(it.player.location, item)
            }
            it.player.inventory.contents = emptyArray()
            players.remove(it.entity.uniqueId)

            broadcast(
                it.deathMessage()?.style(Style.style(NamedTextColor.RED)) ?: Component.text(
                    it.player.name + " died!",
                    NamedTextColor.RED
                )
            )

            it.entity.killer?.let { killer ->
                stats.addKill(killer, it.entity)
                scoreboardManager.reloadKills(killer)
            }
            onlinePlayers.forEach { player ->
                scoreboardManager.reloadAlivePlayers(player)
            }

            if (players.size <= 1) {
                gameState = WinState(this, players.firstOrNull() ?: it.entity.uniqueId)
            }
        }

        listen<BlockPlaceEvent> {
            if (gameState is SetupState) {
                it.isCancelled = false
                return@listen
            }

            if (it.block.state is Container) {
                it.isCancelled = true
                return@listen
            }

            placedBlocks.add(it.block.location)
        }

        listen<BlockBreakEvent> {
            if (gameState is SetupState) {
                it.isCancelled = false
                return@listen
            }

            it.isCancelled = it.block.location !in placedBlocks
        }

        world.worldBorder.center = plugin.gameConfig.centerLocation
        world.worldBorder.size = plugin.gameConfig.borderStartingSize
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
            player.scoreboard.teams.forEach { team ->
                if (team.name in onlinePlayers.map { it.name }) {
                    team.unregister()
                }
            }

            onlinePlayers.forEach { other ->
                val team = player.scoreboard.registerNewTeam(other.name)
                team.displayName(Component.text(other.name))
                team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER)
                team.addEntry(other.name)
            }

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

            scoreboardManager.reloadAlivePlayers(player)
        }
    }

}