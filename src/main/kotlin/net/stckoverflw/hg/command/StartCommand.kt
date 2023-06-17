package net.stckoverflw.hg.command

import net.axay.kspigot.commands.command
import net.axay.kspigot.commands.runs
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.stckoverflw.hg.HungerGamesPlugin
import net.stckoverflw.hg.game.state.WaitingState

fun startCommand(plugin: HungerGamesPlugin) = command("start") {
    runs {
        if (plugin.game.gameState !is WaitingState) {
            sender.sendSystemMessage(Component.literal("Game is already running").withStyle(ChatFormatting.RED))
            return@runs
        }

        (plugin.game.gameState as WaitingState).startCountdown()
    }
}