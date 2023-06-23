package net.stckoverflw.hg.game.state

import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.runnables.task
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.stckoverflw.hg.game.HungerGamesGame
import org.bukkit.WeatherType

class EndgameState(val game: HungerGamesGame) : GameState() {

    private var timeLeft = 300

    override fun start() {
        timeLeft = 300

        onlinePlayers.forEach {
            it.setPlayerWeather(WeatherType.DOWNFALL)
            it.sendMessage(Component.text("The Endgame has started! You have 5 minutes to fight!", NamedTextColor.RED))
        }

        task(
            sync = false,
            delay = 0L,
            period = 20L
        ) {
            timeLeft--

            onlinePlayers.forEach {
                game.scoreboardManager.setTime(
                    it,
                    Component.text("The End", NamedTextColor.GRAY, TextDecoration.BOLD),
                    game.miniMessage.deserialize("<red><bold>${timeLeft}s")
                )
            }

            if (timeLeft <= 0) {
                game.gameState = ChaosState(game)
                it.cancel()
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