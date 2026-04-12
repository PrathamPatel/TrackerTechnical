package com.tracker.trackertechnical.model.dto.shipment

data class ShipmentDetailDto(
    val id: String,
    val carrier: ShipmentDto.CarrierDto,
    val trackingNumber: String,
    val origin: ShipmentDto.LocationDto? = null,
    val destination: ShipmentDto.LocationDto? = null,
    val estimatedDeliveryAt: String? = null,
    val statuses: List<DetailStatusDto>? = null,
) {
    data class DetailStatusDto(
        val time: String,
        val code: String,
        val label: String,
        val location: String? = null,
    )
}
