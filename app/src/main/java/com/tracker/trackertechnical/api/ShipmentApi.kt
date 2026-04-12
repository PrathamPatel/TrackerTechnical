package com.tracker.trackertechnical.api

import com.tracker.trackertechnical.model.dto.shipment.ShipmentDetailDto
import com.tracker.trackertechnical.model.dto.shipment.ShipmentsResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ShipmentApi {

    @GET("shipments")
    suspend fun getShipments() : ShipmentsResponseDto

    @GET("shipments/{id}")
    suspend fun getShipmentDetail(@Path("id") id: String): ShipmentDetailDto
}
