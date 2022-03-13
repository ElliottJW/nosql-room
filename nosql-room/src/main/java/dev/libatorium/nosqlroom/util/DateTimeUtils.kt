package dev.libatorium.nosqlroom.util

import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtils {

    const val ISO8601_STANDARD_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

    private val ISO8601_DATE_FORMATTER = SimpleDateFormat(ISO8601_STANDARD_DATE_TIME_FORMAT, Locale.ROOT)

}