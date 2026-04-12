package com.tracker.trackertechnical.extensions

import com.tracker.trackertechnical.R
import com.tracker.trackertechnical.constants.Constants

fun Constants.ErrorType.toMessage() : Int{
    return when(this){
        Constants.ErrorType.EMPTY -> R.string.shipments_empty_no_shipments_to_display
        Constants.ErrorType.NO_CACHE -> R.string.no_cached_shipments
        Constants.ErrorType.FAILED_REFRESH -> R.string.refresh_failed_showing_cached_data
        Constants.ErrorType.GENERAL -> R.string.something_unusual_has_occurred
    }
}