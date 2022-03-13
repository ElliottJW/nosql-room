package dev.libatorium.nosqlroom.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import java.util.*

/**
 * Room needs a specific way to serialize / deserialize Dates. To keep it simple, we chose Long.
 */
@ProvidedTypeConverter
class DateConverter {

    @TypeConverter
    fun fromTimestamp(value: Long?) : Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun toTimestamp(value: Date?) : Long? {
        return value?.time
    }
}