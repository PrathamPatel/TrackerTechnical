package com.tracker.trackertechnical.extensions

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun String.formatIsoUtcDate(pattern : String = "yyyy-MM-dd HH:mm:ss") : String{
    return try {
        val instant = Instant.parse(this)
        val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
            .withZone(ZoneId.systemDefault())

        formatter.format(instant)
    } catch (_: DateTimeParseException) {
        this
    }
}