package net.stckoverflw.hg.game.state

import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.runnables.KSpigotRunnable
import net.axay.kspigot.runnables.task
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.title.Title
import net.kyori.adventure.util.Ticks
import net.stckoverflw.hg.game.HungerGamesGame
import net.stckoverflw.hg.util.formatTime
import org.bukkit.GameMode
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

class GracePeriod(val game: HungerGamesGame) : GameState() {

    private var timeLeft = game.plugin.gameConfig.gracePeriod

    private var task: KSpigotRunnable? = null

    override fun start() {
        game.chestFilling.refillChests()

        val times = Title.Times.times(Ticks.duration(10), 1.seconds.toJavaDuration(), Ticks.duration(15))
        val mainTitle = Component.text("Go! Go! Go!", NamedTextColor.RED, TextDecoration.BOLD)
        val subtitle = Component.text("You have ", NamedTextColor.GRAY)
            .append(Component.text(game.plugin.gameConfig.gracePeriod.formatTime(), NamedTextColor.BLUE)).append(
                Component.text(" of invincibility", NamedTextColor.GRAY)
            )

        val title = Title.title(mainTitle, subtitle, times)

        onlinePlayers.forEach {
            it.player?.gameMode = GameMode.SURVIVAL
            it.playSound(it.location, Sound.ENTITY_BLAZE_SHOOT, 1f, 1f)
            it.showTitle(title)
            it.sendMessage(
                game.miniMessage.deserialize(
                    "<gray>Grace period has <blue>started<gray>! <gray>You have <blue>${game.plugin.gameConfig.gracePeriod.formatTime()} <gray>of invincibility."
                )
            )
        }

        stateListen<EntityDamageEvent> {
            if (it.entity is Player) {
                it.isCancelled = true
            }
        }

        timeLeft = game.plugin.gameConfig.gracePeriod

        task = task(
            sync = false,
            delay = 0L,
            period = 20L,
        ) {
            timeLeft--

            onlinePlayers.forEach {
                game.scoreboardManager.setTime(
                    it,
                    game.miniMessage.deserialize("<gray>Grace period ends in"),
                    game.miniMessage.deserialize("<blue>${timeLeft.formatTime()}")
                )
            }

            if (timeLeft <= 0) {
                game.gameState = ExploreState(game)
                return@task
            }
        }
    }

    override fun stop() {
        task?.cancel()
        task = null

        onlinePlayers.forEach {
            it.playSound(it.location, Sound.ENTITY_WITHER_SPAWN, 1f, 1f)
            it.sendMessage(
                game.miniMessage.deserialize(
                    "<gray>Grace period has <red>ended<gray>! <gray>You take damage now."
                )
            )
        }
    }
}