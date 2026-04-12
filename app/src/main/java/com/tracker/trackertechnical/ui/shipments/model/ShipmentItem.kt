package com.tracker.trackertechnical.ui.shipments.model

data class ShipmentItem(
    val id : String,
    val carrierName : String,
    val carrierCode : String,
    val trackingNumber : String,
    val statusLabel : String,
    val lastUpdatedText : String
)
