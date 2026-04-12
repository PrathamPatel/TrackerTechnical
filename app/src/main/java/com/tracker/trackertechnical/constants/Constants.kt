package com.tracker.trackertechnical.constants

import com.tracker.trackertechnical.model.local.shipment.ShipmentDetail
import com.tracker.trackertechnical.ui.shipments.model.ShipmentItem

object Constants {

    enum class ErrorType{
        EMPTY, NO_CACHE, FAILED_REFRESH, GENERAL
    }
    val PREVIEW_DATA =
        listOf(
            ShipmentItem(
                id = "shp_1001",
                carrierName = "UPS",
                carrierCode = "UPS",
                trackingNumber = "1Z999AA10123456784",
                statusLabel = "In transit",
                lastUpdatedText = "2026-02-24T08:15:00Z"
            ),
            ShipmentItem(
                id = "shp_1002",
                carrierName = "FedEx",
                carrierCode = "FEDEX",
                trackingNumber = "123456789012",
                statusLabel = "Delivered",
                lastUpdatedText = "2026-02-25T14:20:00Z"
            ),
            ShipmentItem(
                id = "shp_1003",
                carrierName = "DHL",
                carrierCode = "DHL",
                trackingNumber = "JD0146000038282828",
                statusLabel = "Out for delivery",
                lastUpdatedText = "2026-02-27T07:45:00Z"
            )
        )

    val PREVIEW_SHIPMENT_DETAIL = ShipmentDetail(
        id = "shp_1001",
        carrierCode = "UPS",
        carrierName = "UPS",
        trackingNumber = "1Z999AA10123456784",
        originCity = "Seattle",
        originCountry = "US",
        destinationCity = "Austin",
        destinationCountry = "US",
        estimatedDeliveryAt = "2026-03-01T12:00:00Z",
        statuses = listOf(
            ShipmentDetail.ShipmentStatusEvent(
                timeIso = "2026-02-25T10:00:00Z",
                code = "IN_TRANSIT",
                label = "In transit",
                location = "Memphis, TN",
            ),
            ShipmentDetail.ShipmentStatusEvent(
                timeIso = "2026-02-24T08:00:00Z",
                code = "LABEL_CREATED",
                label = "Label created",
                location = "Seattle, WA",
            ),
        ),
        isTimelinePartial = false,
    )
}