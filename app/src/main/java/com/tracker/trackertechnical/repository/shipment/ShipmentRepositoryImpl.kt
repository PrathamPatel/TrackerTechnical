package com.tracker.trackertechnical.repository.shipment

import com.tracker.trackertechnical.api.ShipmentApi
import com.tracker.trackertechnical.extensions.toEntity
import com.tracker.trackertechnical.extensions.toShipment
import com.tracker.trackertechnical.extensions.toShipmentDetail
import com.tracker.trackertechnical.extensions.toShipmentDetailFallback
import com.tracker.trackertechnical.model.local.shipment.Shipment
import com.tracker.trackertechnical.model.local.shipment.ShipmentDetail
import com.tracker.trackertechnical.model.local.shipment.ShipmentDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ShipmentRepositoryImpl @Inject constructor(
    private val api : ShipmentApi,
    private val shipmentDao : ShipmentDao
) : ShipmentRepository {

    override fun getShipmentsLocal(): Flow<List<Shipment>> {
        return shipmentDao.getShipments().map { entities ->
            entities.map { it.toShipment() }
        }
    }

    override suspend fun updateShipmentsRemote(): Result<Unit> {
        return runCatching {
            val response = api.getShipments()
            val entities = response.shipments.map { it.toEntity() }

            shipmentDao.clearAll()
            shipmentDao.insertAll(entities)
        }
    }

    override suspend fun getShipmentDetail(id: String): Result<ShipmentDetail> {
        return runCatching {
            val response = api.getShipmentDetail(id)
            val entity = response.toShipmentDetail()
            Result.success(entity)
        }.getOrElse {
            val cached = shipmentDao.getById(id)
            if(cached != null){
                Result.success(cached.toShipmentDetailFallback())
            }
            else{
                throw IllegalStateException("No shipment detail for id=$id")
            }
        }
    }
}