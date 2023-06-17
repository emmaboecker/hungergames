package net.stckoverflw.hg.game

import org.bukkit.entity.Player
import java.util.*

class Stats(game: HungerGamesGame) {

    private val kills = mutableMapOf<UUID, List<UUID>>()

    fun getKills(player: Player): List<UUID> {
        return this.kills[player.uniqueId] ?: emptyList()
    }

    fun addKill(player: Player, killed: Player) {
        val list = this.kills[player.uniqueId]?.toMutableList() ?: mutableListOf()
        list.add(killed.uniqueId)
        this.kills[player.uniqueId] = list
    }

}