package net.stckoverflw.hg.game.state

import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.runnables.task
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.stckoverflw.hg.game.HungerGamesGame
import net.stckoverflw.hg.util.formatTime
import org.bukkit.WeatherType

class EndgameState(val game: HungerGamesGame) : GameState() {

    private var timeLeft = 600

    override fun start() {
        timeLeft = 600

        onlinePlayers.forEach {
            it.setPlayerWeather(WeatherType.DOWNFALL)
            it.sendMessage(game.miniMessage.deserialize("<redThe Endgame has started! You have <bold>10</bold> minutes to fight!"))
        }

        task(
            sync = false,
            delay = 0L,
            period = 20L
        ) { runnable ->
            timeLeft--

            onlinePlayers.forEach {
                game.scoreboardManager.setTime(
                    it,
                    Component.text("The End", NamedTextColor.GRAY, TextDecoration.BOLD),
                    game.miniMessage.deserialize("<red><bold>${timeLeft.formatTime()}")
                )
            }

            if (timeLeft <= 0) {
                game.gameState = ChaosState(game)
                runnable.cancel()
                return@task
            }
        }

    }

    override fun stop() {
        onlinePlayers.forEach {
            it.setPlayerWeather(WeatherType.CLEAR)
        }
    }

}