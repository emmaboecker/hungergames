package net.stckoverflw.hg.game.state

import net.axay.kspigot.extensions.onlinePlayers
import net.stckoverflw.hg.game.HungerGamesGame

class FightState(val game: HungerGamesGame) : GameState() {
    override fun start() {
        onlinePlayers.forEach {
            it.sendMessage(
                game.miniMessage.deserialize(
                    "<gray>The Border is now <red>shrinking<gray>!"
                )
            )
        }

        game.world.worldBorder.setSize(game.plugin.gameConfig.borderEndingSize, game.plugin.gameConfig.borderShrinkTime)
    }

    override fun stop() {

    }
}