package net.stckoverflw.hg.game.state

import net.stckoverflw.hg.game.HungerGamesGame
import org.bukkit.entity.EntityType

class ChaosState(val game: HungerGamesGame) : GameState() {
    override fun start() {

        game.plugin.gameConfig.spawnLocations.forEach {
            game.world.strikeLightning(it)
            game.world.strikeLightning(it)
            game.world.strikeLightning(it)
            game.world.strikeLightning(it)

            game.world.spawnEntity(it, EntityType.RAVAGER)
        }
    }

    override fun stop() {

    }


}