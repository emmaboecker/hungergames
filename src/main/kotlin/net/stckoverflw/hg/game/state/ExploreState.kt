package net.stckoverflw.hg.game.state

import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.runnables.KSpigotRunnable
import net.axay.kspigot.runnables.task
import net.stckoverflw.hg.game.HungerGamesGame
import net.stckoverflw.hg.util.formatTime

class ExploreState(val game: HungerGamesGame) : GameState() {

    private var timeLeft = game.plugin.gameConfig.borderStartShrinking

    private var task: KSpigotRunnable? = null

    override fun start() {
        task = task(
            sync = false,
            delay = 0L,
            period = 20L
        ) {
            if (timeLeft <= 0) {
                game.gameState = FightState(game)
                it.cancel()
                return@task
            }

            onlinePlayers.forEach { player ->
                game.scoreboardManager.setTime(
                    player,
                    game.miniMessage.deserialize("<gray>Border starts shrinking"),
                    game.miniMessage.deserialize("<blue>${timeLeft.formatTime()}")
                )
            }

            timeLeft--
        }
    }

    override fun stop() {
        task?.cancel()
        task = null
    }
}