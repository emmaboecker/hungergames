package net.stckoverflw.hg.config

import kotlinx.serialization.Serializable
import net.stckoverflw.hg.util.SerializableLocation

@Serializable
data class GameConfig(
    val worldName: String = "world",
    val spawnLocations: List<SerializableLocation> = emptyList(),
    val centerLocation: SerializableLocation,
    val borderStartShrinking: Int = 5 * 60,
    val borderStartingSize: Double = 500.0,
    val borderEndingSize: Double = 50.0,
    val borderShrinkTime: Long = 20 * 60,
    val gracePeriod: Int = 60,
    val startCountdown: Int = 30,
    val refillPeriod: Int = 300,
)
