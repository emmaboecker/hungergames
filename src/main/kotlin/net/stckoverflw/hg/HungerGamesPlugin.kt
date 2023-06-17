package net.stckoverflw.hg

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.axay.kspigot.main.KSpigot
import net.stckoverflw.hg.command.setupCommand
import net.stckoverflw.hg.command.spectateCommand
import net.stckoverflw.hg.command.startCommand
import net.stckoverflw.hg.config.GameConfig
import net.stckoverflw.hg.game.HungerGamesGame
import java.nio.file.Path
import kotlin.io.path.*

class HungerGamesPlugin : KSpigot() {

    lateinit var game: HungerGamesGame

    lateinit var gameConfig: GameConfig

    private lateinit var configFile: Path

    val json = Json {
        prettyPrint = true
        encodeDefaults = true
    }

    private var initConfig = false

    override fun load() {
        val dataFolder = dataFolder.toPath()
        dataFolder.createDirectories()
        configFile = dataFolder.resolve("config.json")
        if (!configFile.exists()) {
            configFile.createFile()
            initConfig = true
        }
    }

    override fun startup() {
        if (initConfig) {
            configFile.writeText(json.encodeToString(GameConfig(centerLocation = server.getWorld("world")!!.spawnLocation)))
        }

        gameConfig = json.decodeFromString(configFile.readText())

        game = HungerGamesGame(this, server.getWorld("world")!!)

        setupCommand(this)
        startCommand(this)
        spectateCommand(this)
    }

    fun reloadConfig(newConfig: GameConfig? = null) {
        if (newConfig != null) {
            gameConfig = newConfig
            configFile.writeText(json.encodeToString(newConfig))
        } else {
            gameConfig = json.decodeFromString(configFile.readText())
        }
    }

}