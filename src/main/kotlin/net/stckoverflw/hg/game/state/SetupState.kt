package net.stckoverflw.hg.game.state

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.stckoverflw.hg.game.HungerGamesGame
import org.bukkit.GameMode
import org.bukkit.block.Container
import org.bukkit.event.EventPriority
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent

class SetupState(val game: HungerGamesGame) : GameState() {
    override fun start() {
        game.plugin.server.broadcast(Component.text("Switching to setup state", NamedTextColor.AQUA))

        stateListen<PlayerJoinEvent>(priority = EventPriority.HIGH) { event ->
            event.player.gameMode = GameMode.CREATIVE
        }

        stateListen<BlockBreakEvent>(priority = EventPriority.HIGH) {
            it.isCancelled = !it.player.isOp
        }

        stateListen<BlockPlaceEvent>(priority = EventPriority.HIGH) {
            it.isCancelled = !it.player.isOp
        }

        stateListen<PlayerInteractEvent> {
            if (it.clickedBlock != null) {
                if (it.clickedBlock!!.state is Container) {
                    val container = it.clickedBlock!!.state as Container
                    container.inventory.contents = container.inventory.contents.map { _ ->
                        game.chestFilling.getItem(3)
                    }.toTypedArray()
                }
            }
        }
    }

    override fun stop() {

    }
}