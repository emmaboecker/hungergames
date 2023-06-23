package net.stckoverflw.hg.game.state

import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.runnables.KSpigotRunnable
import net.axay.kspigot.runnables.task
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.stckoverflw.hg.game.HungerGamesGame
import org.bukkit.Sound

class FightState(val game: HungerGamesGame) : GameState() {

    private var task: KSpigotRunnable? = null

    private var seconds = 0

    override fun start() {
        onlinePlayers.forEach {
            it.sendMessage(
                game.miniMessage.deserialize(
                    "<gray>The Border is now <red>shrinking<gray>!"
                )
            )
        }

        game.world.worldBorder.setSize(
            game.plugin.gameConfig.borderEndingSize,
            game.plugin.gameConfig.borderShrinkTime
        )

        val refillTime = game.plugin.gameConfig.refillPeriod

        task = task(
            sync = false,
            delay = 0L,
            period = 20L
        ) {
            seconds++

            if (seconds % refillTime == 0) {
                game.chestFilling.tries += (seconds / refillTime) * 2
                game.chestFilling.armourLimit += (seconds / refillTime) + 2
                game.chestFilling.enchantableLimit += (seconds / refillTime) + 2
                game.chestFilling.refillChests()
            }

            onlinePlayers.forEach {
                if (seconds % refillTime == 0) {
                    it.sendMessage(Component.text("Chests have been refilled!"))
                    it.playSound(it, Sound.BLOCK_CHEST_OPEN, 1f, 1f)
                }

                game.scoreboardManager.reloadBorder(it)

                game.scoreboardManager.setTime(
                    it,
                    Component.text("Next Chest refill", NamedTextColor.GRAY),
                    game.miniMessage.deserialize("<blue>${refillTime - (seconds % refillTime)}s")
                )
            }

            if (game.world.worldBorder.size <= game.plugin.gameConfig.borderEndingSize) {
                game.gameState = EndgameState(game)
                return@task
            }
        }
    }

    override fun stop() {
        task?.cancel()
        task = null
    }
}