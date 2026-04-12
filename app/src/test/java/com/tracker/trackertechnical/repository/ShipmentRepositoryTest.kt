package com.tracker.trackertechnical.repository

import com.tracker.trackertechnical.api.ShipmentApi
import com.tracker.trackertechnical.model.dto.shipment.ShipmentDetailDto
import com.tracker.trackertechnical.model.dto.shipment.ShipmentDto
import com.tracker.trackertechnical.model.dto.shipment.ShipmentsResponseDto
import com.tracker.trackertechnical.model.local.shipment.ShipmentDao
import com.tracker.trackertechnical.model.local.shipment.ShipmentEntity
import com.tracker.trackertechnical.repository.shipment.ShipmentRepositoryImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ShipmentRepositoryTest {

    @Test
    fun `updateShipmentsRemote replaces local cache with latest remote data`() = runTest {
        val fakeApi = object : ShipmentApi {
            override suspend fun getShipmentDetail(id: String): ShipmentDetailDto {
                error("not used")
            }

            override suspend fun getShipments(): ShipmentsResponseDto {
                return ShipmentsResponseDto(
                    shipments = listOf(
                        ShipmentDto(
                            id = "shp_1001",
                            carrier = ShipmentDto.CarrierDto("ups", "UPS"),
                            trackingNumber = "1Z999AA10123456784",
                            lastStatus = ShipmentDto.StatusDto("IN_TRANSIT", "In transit"),
                            lastUpdatedAt = "2026-02-25T14:20:00Z",
                            origin = ShipmentDto.LocationDto("Seattle", "US"),
                            destination = ShipmentDto.LocationDto("Austin", "US"),
                            estimatedDeliveryAt = "2026-02-28T18:00:00Z"
                        )
                    )
                )
            }
        }

        val fakeDao = FakeShipmentDao(
            initial = listOf(
                ShipmentEntity(
                    id = "old_1",
                    carrierCode = "fedex",
                    carrierName = "FedEx",
                    trackingNumber = "OLD123",
                    statusCode = "DELIVERED",
                    statusLabel = "Delivered",
                    lastUpdatedAt = "2026-02-20T10:00:00Z",
                    originCity = "Chicago",
                    originCountry = "US",
                    destinationCity = "Boston",
                    destinationCountry = "US",
                    estimatedDeliveryAt = "2026-02-21T18:00:00Z"
                )
            )
        )

        val repository = ShipmentRepositoryImpl(
            api = fakeApi,
            shipmentDao = fakeDao
        )

        val result = repository.updateShipmentsRemote()
        val cached = fakeDao.getShipments().first()

        assertEquals(true, result.isSuccess)
        assertEquals(1, cached.size)
        assertEquals("shp_1001", cached.first().id)
        assertEquals("UPS", cached.first().carrierName)
        assertEquals("IN_TRANSIT", cached.first().statusCode)
    }

    @Test
    fun `getShipmentDetail returns remote timeline sorted newest first`() = runTest {
        val fakeApi = object : ShipmentApi {
            override suspend fun getShipments(): ShipmentsResponseDto = ShipmentsResponseDto(emptyList())

            override suspend fun getShipmentDetail(id: String): ShipmentDetailDto {
                return ShipmentDetailDto(
                    id = id,
                    carrier = ShipmentDto.CarrierDto("ups", "UPS"),
                    trackingNumber = "1Z",
                    origin = ShipmentDto.LocationDto("Seattle", "US"),
                    destination = ShipmentDto.LocationDto("Austin", "US"),
                    estimatedDeliveryAt = "2026-03-02T23:00:00Z",
                    statuses = listOf(
                        ShipmentDetailDto.DetailStatusDto(
                            time = "2026-02-25T08:10:00Z",
                            code = "LABEL_CREATED",
                            label = "Label created",
                            location = "Seattle, WA",
                        ),
                        ShipmentDetailDto.DetailStatusDto(
                            time = "2026-02-26T14:20:00Z",
                            code = "IN_TRANSIT",
                            label = "Departed facility",
                            location = "Portland, OR",
                        ),
                    ),
                )
            }
        }

        val fakeDao = FakeShipmentDao()
        val repository = ShipmentRepositoryImpl(api = fakeApi, shipmentDao = fakeDao)

        val result = repository.getShipmentDetail("shp_1001")
        assertEquals(true, result.isSuccess)
        val detail = result.getOrThrow()
        assertEquals(2, detail.statuses.size)
        assertEquals("IN_TRANSIT", detail.statuses[0].code)
        assertEquals("LABEL_CREATED", detail.statuses[1].code)
        assertEquals(false, detail.isTimelinePartial)
    }

    @Test
    fun `getShipmentDetail uses cached shipment when remote fails`() = runTest {
        val fakeApi = object : ShipmentApi {
            override suspend fun getShipments(): ShipmentsResponseDto = ShipmentsResponseDto(emptyList())

            override suspend fun getShipmentDetail(id: String): ShipmentDetailDto {
                error("network down")
            }
        }

        val entity = ShipmentEntity(
            id = "shp_1001",
            carrierCode = "ups",
            carrierName = "UPS",
            trackingNumber = "1Z999",
            statusCode = "IN_TRANSIT",
            statusLabel = "In transit",
            lastUpdatedAt = "2026-02-26T14:20:00Z",
            originCity = "Seattle",
            originCountry = "US",
            destinationCity = "Austin",
            destinationCountry = "US",
            estimatedDeliveryAt = "2026-03-02T23:00:00Z",
        )
        val fakeDao = FakeShipmentDao(initial = listOf(entity))
        val repository = ShipmentRepositoryImpl(api = fakeApi, shipmentDao = fakeDao)

        val result = repository.getShipmentDetail("shp_1001")
        assertEquals(true, result.isSuccess)
        val detail = result.getOrThrow()
        assertEquals(true, detail.isTimelinePartial)
        assertEquals(1, detail.statuses.size)
        assertEquals("IN_TRANSIT", detail.statuses[0].code)
    }
}

private class FakeShipmentDao(
    initial: List<ShipmentEntity> = emptyList()
) : ShipmentDao {

    private val state = MutableStateFlow(initial)

    override fun getShipments(): Flow<List<ShipmentEntity>> = state

    override suspend fun insertAll(items: List<ShipmentEntity>) {
        state.value = items
    }

    override suspend fun clearAll() {
        state.value = emptyList()
    }

    override suspend fun getById(id: String): ShipmentEntity? = state.value.find { it.id == id }
}