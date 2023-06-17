package net.stckoverflw.hg.game.state

import net.axay.kspigot.runnables.task
import net.stckoverflw.hg.game.HungerGamesGame

class ExploreState(val game: HungerGamesGame) : GameState() {

    var seconds = game.plugin.gameConfig.borderStartShrinking

    override fun start() {
        task(
            sync = false,
            delay = 0L,
            period = 20L
        ) {
            if (seconds <= 0) {
                game.gameState = GracePeriod(game)
                it.cancel()
                return@task
            }

            seconds--
        }
    }

    override fun stop() {

    }
}