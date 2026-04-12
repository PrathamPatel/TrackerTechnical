package com.tracker.trackertechnical.util

object LocationUtil {
    fun formatLocation(city: String?, country: String?): String? {
        return when {
            !city.isNullOrBlank() && !country.isNullOrBlank() -> "$city, $country"
            !city.isNullOrBlank() -> city
            !country.isNullOrBlank() -> country
            else -> null
        }
    }
}