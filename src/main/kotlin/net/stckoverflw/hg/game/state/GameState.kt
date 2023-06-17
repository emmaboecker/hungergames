package net.stckoverflw.hg.game.state

import net.axay.kspigot.event.SingleListener
import net.axay.kspigot.event.listen
import net.axay.kspigot.event.unregister
import org.bukkit.event.Event
import org.bukkit.event.EventPriority

sealed class GameState {

    protected val listeners = mutableListOf<SingleListener<*>>()

    abstract fun start()

    fun internalStop() {
        listeners.forEach { it.unregister() }
        stop()
    }

    abstract fun stop()

    protected inline fun <reified T : Event> stateListen(
        priority: EventPriority = EventPriority.NORMAL,
        ignoreCancelled: Boolean = false,
        register: Boolean = true,
        crossinline onEvent: (event: T) -> Unit,
    ): SingleListener<T> {
        val listener = listen<T>(priority, ignoreCancelled, register, onEvent)
        this.listeners += listener
        return listener
    }

}
