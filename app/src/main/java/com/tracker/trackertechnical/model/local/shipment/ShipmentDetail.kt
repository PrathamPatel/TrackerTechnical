package com.tracker.trackertechnical.model.local.shipment
data class ShipmentDetail(
    val id: String,
    val carrierCode: String,
    val carrierName: String,
    val trackingNumber: String,
    val originCity: String?,
    val originCountry: String?,
    val destinationCity: String?,
    val destinationCountry: String?,
    val estimatedDeliveryAt: String?,
    val statuses: List<ShipmentStatusEvent>,
    val isTimelinePartial: Boolean,
){
    data class ShipmentStatusEvent(
        val timeIso: String,
        val code: String,
        val label: String,
        val location: String?,
    )

}
