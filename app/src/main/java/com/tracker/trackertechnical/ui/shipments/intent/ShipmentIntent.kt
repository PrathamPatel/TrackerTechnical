package com.tracker.trackertechnical.ui.shipments.intent

sealed interface ShipmentIntent {
    data object LoadInitial : ShipmentIntent
    data object RefreshClicked : ShipmentIntent
    data object RetryClicked : ShipmentIntent
}