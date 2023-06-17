package net.stckoverflw.hg.game.state

import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.runnables.KSpigotRunnable
import net.axay.kspigot.runnables.task
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.stckoverflw.hg.game.HungerGamesGame
import org.bukkit.GameMode
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerMoveEvent

class WaitingState(val game: HungerGamesGame) : GameState() {

    private var countdownTask: KSpigotRunnable? = null

    override fun start() {
        stateListen<PlayerJoinEvent> {
            if (game.plugin.gameConfig.spawnLocations.isEmpty()) {
                it.player.sendMessage(Component.text("There are no more spawn locations available, this game has not been configured yet."))
                game.gameState = SetupState(game)
                return@stateListen
            }

            val spawnLocation =
                game.plugin.gameConfig.spawnLocations[game.players.indexOf(it.player.uniqueId) % game.plugin.gameConfig.spawnLocations.size]

            it.player.teleport(spawnLocation)
            it.player.gameMode = GameMode.ADVENTURE
        }

        stateListen<PlayerMoveEvent> {
            if (it.hasChangedBlock()) {
                it.isCancelled = true
            }
        }


    }

    override fun stop() {
    }

    fun startCountdown(): Boolean {
        if (countdownTask != null) {
            countdownTask!!.cancel()
            countdownTask = null
            return false
        }

        var seconds = game.plugin.gameConfig.startCountdown

        countdownTask = task(
            sync = false,
            period = 20L,
            delay = 0L,
        ) {
            if (seconds <= 0) {
                game.gameState = GracePeriod(game)
                return@task
            }

            onlinePlayers.forEach {
                it.sendActionBar(
                    Component.text(
                        "The game will start in $seconds seconds",
                        NamedTextColor.GOLD
                    )
                )
            }

            seconds--

        }

        return true
    }
}