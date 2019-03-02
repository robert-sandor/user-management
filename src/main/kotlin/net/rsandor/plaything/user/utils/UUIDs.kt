package net.rsandor.plaything.user.utils

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import kotlinx.io.errors.IOException
import org.apache.commons.codec.binary.Base64
import java.nio.ByteBuffer
import java.util.*

object UUIDs {
    fun encode(uuid: UUID): String {
        val byteBuffer = ByteBuffer.wrap(ByteArray(16))
        byteBuffer.putLong(uuid.mostSignificantBits)
        byteBuffer.putLong(uuid.leastSignificantBits)
        return Base64.encodeBase64URLSafeString(byteBuffer.array())
    }

    fun decode(uuidString: String?): UUID? {
        return when (uuidString?.length) {
            36 -> UUID.fromString(uuidString)
            22 -> {
                val byteBuffer = ByteBuffer.wrap(Base64.decodeBase64(uuidString))
                when (byteBuffer.capacity()) {
                    16 -> UUID(byteBuffer.long, byteBuffer.long)
                    else -> null
                }
            }
            else -> null
        }
    }

    class UuidTypeAdapter : TypeAdapter<UUID>() {
        override fun write(out: JsonWriter?, value: UUID?) {
            when (value) {
                null -> out?.nullValue()
                else -> out?.value(UUIDs.encode(value))
            }
        }

        override fun read(reader: JsonReader?): UUID {
            val decodedUuid = UUIDs.decode(reader?.nextString())
            return when (decodedUuid) {
                null -> throw IOException("Invalid UUID string literal found!")
                else -> decodedUuid
            }
        }

    }
}