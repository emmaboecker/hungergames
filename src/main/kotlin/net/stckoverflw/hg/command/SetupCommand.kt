package net.stckoverflw.hg.command

import net.axay.kspigot.commands.command
import net.axay.kspigot.commands.literal
import net.axay.kspigot.commands.runs
import net.minecraft.network.chat.Component
import net.stckoverflw.hg.HungerGamesPlugin
import net.stckoverflw.hg.game.state.SetupState

fun setupCommand(plugin: HungerGamesPlugin) = command("setup") {
    requires {
        it.source.getBukkitSender(it).isOp
    }
    literal("center") {
        runs {
            val player = sender.player ?: return@runs
            plugin.reloadConfig(plugin.gameConfig.copy(centerLocation = player.bukkitEntity.location.toCenterLocation()))
            player.sendSystemMessage(Component.literal("Center location set"))
        }
    }
    literal("spawn") {
        literal("add") {
            runs {
                val player = sender.player ?: return@runs
                plugin.reloadConfig(plugin.gameConfig.copy(spawnLocations = plugin.gameConfig.spawnLocations + player.bukkitEntity.location.toCenterLocation()))
                player.sendSystemMessage(Component.literal("Spawn location added (${plugin.gameConfig.spawnLocations.size - 1})"))
            }
        }
    }
    literal("state") {
        runs {
            plugin.game.gameState = SetupState(plugin.game)
        }
    }
    literal("test") {
        literal("spawn") {
            runs {
                val player = sender.player ?: return@runs
                player.bukkitEntity.teleport(plugin.gameConfig.spawnLocations.random())
            }
        }
    }
}