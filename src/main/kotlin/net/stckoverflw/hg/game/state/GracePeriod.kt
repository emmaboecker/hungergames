package net.stckoverflw.hg.game.state

import net.axay.kspigot.extensions.onlinePlayers
import net.stckoverflw.hg.game.HungerGamesGame
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent

class GracePeriod(val game: HungerGamesGame) : GameState() {

    override fun start() {
        onlinePlayers.forEach {
            it.playSound(it.location, Sound.ENTITY_BLAZE_SHOOT, 1f, 1f)
            it.sendMessage(
                game.miniMessage.deserialize(
                    "<gray>Grace period has <blue>started<gray>! <gray>You have <blue>${game.plugin.gameConfig.gracePeriod} seconds <gray>of invincibility."
                )
            )
        }

        stateListen<EntityDamageEvent> {
            if (it.entity is Player) {
                it.isCancelled = true
            }
        }
    }

    override fun stop() {
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