package net.stckoverflw.hg.util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.encodeStructure
import net.axay.kspigot.main.KSpigotMainInstance
import org.bukkit.Location

typealias SerializableLocation = @Serializable(with = LocationSerializer::class) Location

class LocationSerializer : KSerializer<Location> {
    override val descriptor: SerialDescriptor = SerializablePos.serializer().descriptor

    override fun deserialize(decoder: Decoder): Location {
        val input = decoder.decodeSerializableValue(SerializablePos.serializer())
        return Location(
            KSpigotMainInstance.server.getWorld(input.world),
            input.x,
            input.y,
            input.z,
            input.yaw,
            input.pitch
        )
    }

    override fun serialize(encoder: Encoder, value: Location) {
        encoder.encodeStructure(SerializablePos.serializer().descriptor) {
            encodeStringElement(SerializablePos.serializer().descriptor, 0, value.world.name)
            encodeDoubleElement(SerializablePos.serializer().descriptor, 1, value.x)
            encodeDoubleElement(SerializablePos.serializer().descriptor, 2, value.y)
            encodeDoubleElement(SerializablePos.serializer().descriptor, 3, value.z)
            encodeFloatElement(SerializablePos.serializer().descriptor, 4, value.yaw)
            encodeFloatElement(SerializablePos.serializer().descriptor, 5, value.pitch)
        }
    }

    @Serializable
    data class SerializablePos(
        val world: String,
        val x: Double,
        val y: Double,
        val z: Double,
        val yaw: Float,
        val pitch: Float
    )
}