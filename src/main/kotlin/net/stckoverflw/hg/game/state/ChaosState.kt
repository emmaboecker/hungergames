package net.stckoverflw.hg.game.state

import net.axay.kspigot.runnables.task
import net.stckoverflw.hg.game.HungerGamesGame
import org.bukkit.entity.EntityType

class ChaosState(val game: HungerGamesGame) : GameState() {
    override fun start() {

        game.plugin.gameConfig.spawnLocations.forEach {
            task(
                sync = true,
                delay = 0L,
                period = 5L,
                howOften = 3
            ) { _ ->
                game.world.strikeLightning(it)
            }

            game.world.spawnEntity(it, EntityType.RAVAGER)
        }
    }

    override fun stop() {

    }


}