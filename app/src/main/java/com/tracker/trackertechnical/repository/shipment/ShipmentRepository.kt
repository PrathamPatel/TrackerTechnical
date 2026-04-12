package com.tracker.trackertechnical.repository.shipment

import com.tracker.trackertechnical.model.local.shipment.Shipment
import com.tracker.trackertechnical.model.local.shipment.ShipmentDetail
import kotlinx.coroutines.flow.Flow

interface ShipmentRepository {

    fun getShipmentsLocal() : Flow<List<Shipment>>
    suspend fun updateShipmentsRemote() : Result<Unit>
    suspend fun getShipmentDetail(id: String): Result<ShipmentDetail>
}