package com.tracker.trackertechnical.extensions

import com.tracker.trackertechnical.model.dto.shipment.ShipmentDetailDto
import com.tracker.trackertechnical.model.local.shipment.ShipmentDetail
import com.tracker.trackertechnical.model.local.shipment.ShipmentEntity

fun List<ShipmentDetail.ShipmentStatusEvent>.sortedNewestFirst(): List<ShipmentDetail.ShipmentStatusEvent> =
    sortedByDescending { it.timeIso }

fun ShipmentDetailDto.toShipmentDetail(): ShipmentDetail {
    val events = (statuses ?: emptyList()).map { dto ->
        ShipmentDetail.ShipmentStatusEvent(
            timeIso = dto.time,
            code = dto.code,
            label = dto.label,
            location = dto.location,
        )
    }.sortedNewestFirst()
    return ShipmentDetail(
        id = id,
        carrierCode = carrier.code,
        carrierName = carrier.name,
        trackingNumber = trackingNumber,
        originCity = origin?.city,
        originCountry = origin?.country,
        destinationCity = destination?.city,
        destinationCountry = destination?.country,
        estimatedDeliveryAt = estimatedDeliveryAt,
        statuses = events,
        isTimelinePartial = false,
    )
}

fun ShipmentEntity.toShipmentDetailFallback(): ShipmentDetail {
    return ShipmentDetail(
        id = id,
        carrierCode = carrierCode,
        carrierName = carrierName,
        trackingNumber = trackingNumber,
        originCity = originCity,
        originCountry = originCountry,
        destinationCity = destinationCity,
        destinationCountry = destinationCountry,
        estimatedDeliveryAt = estimatedDeliveryAt,
        statuses = listOf(
            ShipmentDetail.ShipmentStatusEvent(
                timeIso = lastUpdatedAt,
                code = statusCode,
                label = statusLabel,
                location = null,
            ),
        ).sortedNewestFirst(),
        isTimelinePartial = true,
    )
}
