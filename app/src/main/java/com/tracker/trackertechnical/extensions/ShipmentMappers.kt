package com.tracker.trackertechnical.extensions

import com.tracker.trackertechnical.model.dto.shipment.ShipmentDto
import com.tracker.trackertechnical.model.local.shipment.Shipment
import com.tracker.trackertechnical.model.local.shipment.ShipmentEntity
import com.tracker.trackertechnical.ui.shipments.model.ShipmentItem

fun ShipmentDto.toEntity() : ShipmentEntity{
    return ShipmentEntity(
        id = this.id,
        carrierCode = carrier.code,
        carrierName = carrier.name,
        trackingNumber = this.trackingNumber,
        statusCode = lastStatus.code,
        statusLabel = lastStatus.label,
        lastUpdatedAt = this.lastUpdatedAt,
        originCity = origin?.city,
        originCountry = origin?.country,
        destinationCity = destination?.city,
        destinationCountry = destination?.country,
        estimatedDeliveryAt = this.estimatedDeliveryAt
    )
}

fun ShipmentEntity.toShipment() : Shipment{
    return Shipment(
        id = this.id,
        carrierCode = this.carrierCode,
        carrierName = this.carrierName,
        trackingNumber = this.trackingNumber,
        statusCode = this.statusCode,
        statusLabel = this.statusLabel,
        lastUpdatedAt = this.lastUpdatedAt,
        originCity = this.originCity,
        originCountry = this.originCountry,
        destinationCity = this.destinationCity,
        destinationCountry = this.destinationCountry,
        estimatedDeliveryAt = this.estimatedDeliveryAt
    )
}

fun Shipment.toUI() : ShipmentItem{
    return ShipmentItem(
        id = this.id,
        carrierName = this.carrierName,
        carrierCode = this.carrierCode,
        trackingNumber = this.trackingNumber,
        statusLabel = this.statusLabel,
        lastUpdatedText = this.lastUpdatedAt,
    )
}