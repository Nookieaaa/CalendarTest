package com.nookdev.calendartest

import com.google.gson.JsonDeserializer
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime


val ZDT_DESERIALIZER: JsonDeserializer<ZonedDateTime> =
    JsonDeserializer { json, typeOfT, context ->
        val jsonPrimitive = json.asJsonPrimitive
        try {
            if (jsonPrimitive.isNumber) {
                return@JsonDeserializer ZonedDateTime.ofInstant(
                    Instant.ofEpochMilli(jsonPrimitive.asLong),
                    ZoneId.systemDefault()
                )
            }
        } catch (e: RuntimeException) {
            throw JsonParseException("Unable to parse ZonedDateTime", e)
        }

        throw JsonParseException("Unable to parse ZonedDateTime")
    }

val ZDT_SERIALIZER: JsonSerializer<ZonedDateTime> = JsonSerializer { src, typeOfSrc, context ->
    JsonPrimitive(src.toInstant().toEpochMilli())
}