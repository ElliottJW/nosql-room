package dev.libatorium.nosqlroom.util

import com.google.gson.*
import timber.log.Timber
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

class GsonUtcDateSerializer(
    dateFormatString: String
) : JsonSerializer<Date>, JsonDeserializer<Date> {

    private val dateFormate = SimpleDateFormat(dateFormatString, Locale.ROOT).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    override fun serialize(
        src: Date?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement? = src?.let { JsonPrimitive(dateFormate.format(it)) }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Date? = json?.let { j ->
        try {
            dateFormate.parse(j.asString)
        } catch (e: JsonParseException) {
            Timber.e(e, "Error parsing date.")
            null
        }
    }
}