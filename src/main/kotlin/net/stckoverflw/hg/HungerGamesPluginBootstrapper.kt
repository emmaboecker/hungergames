package net.stckoverflw.hg

import io.papermc.paper.plugin.bootstrap.BootstrapContext
import io.papermc.paper.plugin.bootstrap.PluginBootstrap
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import java.io.IOException
import kotlin.io.path.*

class HungerGamesPluginBootstrapper : PluginBootstrap {
    @OptIn(ExperimentalPathApi::class)
    override fun bootstrap(context: BootstrapContext) {
        val worldPath = Path("world")

        if (worldPath.exists()) {
            context.logger.info(Component.text("Deleting old world folder...", NamedTextColor.BLUE))
            try {
                worldPath.deleteRecursively()
                context.logger.info(Component.text("Successfully deleted old world folder", NamedTextColor.GREEN))
            } catch (ex: IOException) {
                context.logger.error(Component.text("Failed to delete old world folder!", NamedTextColor.RED))
                context.logger.error(Component.text("${ex.message}:", NamedTextColor.RED))
                context.logger.error(Component.text(ex.stackTraceToString(), NamedTextColor.RED))
            }
        }

        context.logger.info(Component.text("Copying template...", NamedTextColor.BLUE))
        try {
            Path("templates/map").copyToRecursively(
                worldPath.apply { parent?.createDirectories() },
                followLinks = true,
                overwrite = true
            )
            context.logger.info(Component.text("Successfully copied template", NamedTextColor.GREEN))
        } catch (ex: IOException) {
            context.logger.error(Component.text("Failed to copy template!", NamedTextColor.RED))
            context.logger.error(Component.text("${ex.message}:", NamedTextColor.RED))
            context.logger.error(Component.text(ex.stackTraceToString(), NamedTextColor.RED))
        }

    }
}