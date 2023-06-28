package net.stckoverflw.hg.game.state

import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.runnables.KSpigotRunnable
import net.axay.kspigot.runnables.task
import net.kyori.adventure.sound.Sound.Source
import net.kyori.adventure.sound.Sound.sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.title.Title
import net.kyori.adventure.util.Ticks
import net.stckoverflw.hg.game.HungerGamesGame
import org.bukkit.GameMode
import org.bukkit.Sound
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerMoveEvent
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

class WaitingState(val game: HungerGamesGame) : GameState() {

    private var countdownTask: KSpigotRunnable? = null

    override fun start() {
        stateListen<PlayerJoinEvent>(priority = EventPriority.HIGHEST) {
            it.player.inventory.clear()
            it.player.activePotionEffects.clear()

            if (game.plugin.gameConfig.spawnLocations.isEmpty()) {
                it.player.sendMessage(Component.text("There are no more spawn locations available, this game has not been configured yet."))
                game.gameState = SetupState(game)
                return@stateListen
            }

            val spawnLocation =
                game.plugin.gameConfig.spawnLocations[game.players.indexOf(it.player.uniqueId) % game.plugin.gameConfig.spawnLocations.size]

            it.player.teleport(spawnLocation)
            it.player.gameMode = GameMode.ADVENTURE
            it.player.exp = 0F
            it.player.level = 0
            it.player.health = 20.0
            it.player.foodLevel = 20

            game.scoreboardManager.setTime(
                it.player,
                Component.text("Game starts in", NamedTextColor.GRAY),
                Component.text("Waiting...", NamedTextColor.RED)
            )
        }

        stateListen<PlayerMoveEvent> {
            if (it.player.uniqueId !in game.players) {
                return@stateListen
            }

            if (it.hasChangedBlock()) {
                it.isCancelled = true
            }
        }

        stateListen<FoodLevelChangeEvent> {
            it.isCancelled = true
        }
    }

    override fun stop() {
        countdownTask?.cancel()
        countdownTask = null

        val times = Title.Times.times(Ticks.duration(10), 1.seconds.toJavaDuration(), Ticks.duration(15))
        val mainTitle = Component.text("Go! Go! Go!", NamedTextColor.RED, TextDecoration.BOLD)
        val subtitle = Component.text("You have ", NamedTextColor.GRAY)
            .append(Component.text("${game.plugin.gameConfig.gracePeriod} seconds", NamedTextColor.BLUE)).append(
                Component.text(" of invincibility", NamedTextColor.GRAY)
            )

        val title = Title.title(mainTitle, subtitle, times)

        onlinePlayers.forEach {
            it.showTitle(title)
        }
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
            if (seconds % 10 == 0 || seconds <= 5) {
                val mainTitle = Component.text(seconds, NamedTextColor.BLUE, TextDecoration.BOLD)
                val subtitle = Component.text("The Game starts in ", NamedTextColor.GRAY)
                    .append(Component.text(seconds, NamedTextColor.RED)).append(
                        Component.text(" seconds", NamedTextColor.GRAY)
                    )

                val title = Title.title(mainTitle, subtitle)

                onlinePlayers.forEach {
                    if (seconds >= 4) {
                        it.playSound(sound(Sound.BLOCK_NOTE_BLOCK_PLING, Source.AMBIENT, 1f, 0f), it)
                    } else if (seconds in 1..3) {
                        it.playSound(sound(Sound.BLOCK_NOTE_BLOCK_PLING, Source.AMBIENT, 1f, 1f), it)
                    } else {
                        it.playSound(sound(Sound.BLOCK_NOTE_BLOCK_PLING, Source.AMBIENT, 1f, 2f), it)
                    }
                    it.showTitle(title)
                }
            }

            onlinePlayers.forEach { player ->
                game.scoreboardManager.setTime(
                    player,
                    Component.text("Game starts in", NamedTextColor.GRAY),
                    Component.text("${seconds}s", NamedTextColor.RED)
                )
            }

            if (seconds <= 0) {
                game.gameState = GracePeriod(game)
                return@task
            }

            seconds--
        }

        return true
    }
}