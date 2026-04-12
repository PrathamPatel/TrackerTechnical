package com.tracker.trackertechnical.model.dto.shipment

data class ShipmentDto(
    val id: String,
    val carrier: CarrierDto,
    val trackingNumber: String,
    val lastStatus: StatusDto,
    val lastUpdatedAt: String,
    val origin: LocationDto? = null,
    val destination: LocationDto? = null,
    val estimatedDeliveryAt: String? = null
){
    data class CarrierDto(
        val code: String,
        val name: String
    )
    data class StatusDto(
        val code: String,
        val label: String
    )

    data class LocationDto(
        val city: String? = null,
        val country: String? = null
    )
}