package net.stckoverflw.hg.game.state

import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.runnables.task
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.title.Title
import net.stckoverflw.hg.game.HungerGamesGame
import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Firework
import org.bukkit.entity.Item
import java.util.*


class WinState(val game: HungerGamesGame, private val winner: UUID) : GameState() {
    override fun start() {
        game.plugin.server.getPlayer(winner)?.let { winner ->
            val mainTitle = Component.text(winner.name, NamedTextColor.BLUE, TextDecoration.BOLD)
            val subtitle = Component.text("has won the game!", NamedTextColor.GRAY)

            val title = Title.title(mainTitle, subtitle)

            onlinePlayers.forEach {
                it.teleport(winner)
                it.sendMessage(
                    game.miniMessage.deserialize("<gray>The game has <blue>ended<gray>! The winner is <blue>${winner.name}<gray>!")
                )
                it.showTitle(title)
            }
        }


        task(
            sync = true,
            delay = 0L,
            period = 20L,
            howOften = 10
        ) {
            game.plugin.gameConfig.spawnLocations.forEach {
                val firework = game.world.spawnEntity(it, EntityType.FIREWORK) as Firework
                val meta = firework.fireworkMeta

                meta.power = 2
                meta.addEffect(FireworkEffect.builder().withColor(Color.RED, Color.BLUE).trail(true).build())

                firework.fireworkMeta = meta
            }
        }

        game.placedBlocks.forEach {
            val block = it.world.getBlockAt(it)
            block.type = Material.AIR
            block.state.type = Material.AIR
            block.state.update(true, true)
        }

        game.world.entities.forEach {
            if (it is Item) {
                it.remove()
            }
        }
    }

    override fun stop() {

    }
}