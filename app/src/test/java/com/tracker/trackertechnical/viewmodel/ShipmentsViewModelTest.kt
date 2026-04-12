package com.tracker.trackertechnical.viewmodel

import com.tracker.trackertechnical.R
import com.tracker.trackertechnical.model.local.shipment.Shipment
import com.tracker.trackertechnical.model.local.shipment.ShipmentDetail
import com.tracker.trackertechnical.repository.network.NetworkMonitor
import com.tracker.trackertechnical.repository.shipment.ShipmentRepository
import com.tracker.trackertechnical.ui.shipments.ShipmentsViewModel
import com.tracker.trackertechnical.ui.shipments.intent.ShipmentIntent
import com.tracker.trackertechnical.ui.shipments.state.ShipmentContentState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ShipmentsViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `refresh failure with cached data keeps content and shows banner`() = kotlinx.coroutines.test.runTest {
        val cachedShipments = listOf(
            Shipment(
                id = "shp_1001",
                carrierCode = "ups",
                carrierName = "UPS",
                trackingNumber = "1Z999AA10123456784",
                statusCode = "IN_TRANSIT",
                statusLabel = "In transit",
                lastUpdatedAt = "2026-02-25T14:20:00Z",
                originCity = "Seattle",
                originCountry = "US",
                destinationCity = "Austin",
                destinationCountry = "US",
                estimatedDeliveryAt = "2026-02-28T18:00:00Z"
            )
        )

        val fakeRepository = FakeShipmentRepository(
            initialShipments = cachedShipments,
            remoteResult = Result.failure(RuntimeException("Server error"))
        )

        val fakeNetworkMonitor = object : NetworkMonitor {
            override fun isOnline(): Boolean = true
        }

        val viewModel = ShipmentsViewModel(
            shipmentRepository = fakeRepository,
            networkMonitor = fakeNetworkMonitor
        )

        runCurrent()

        viewModel.handleIntent(ShipmentIntent.RefreshClicked)
        runCurrent()

        val state = viewModel.state.value

        assertTrue(state.shipments.isNotEmpty())
        assertEquals(ShipmentContentState.Content, state.contentState)
        assertEquals(R.string.could_not_refresh_showing_saved_data, state.bannerMessage)
        assertEquals(false, state.isRefreshing)
        assertEquals(false, state.isLoading)
    }
}

private class FakeShipmentRepository(
    initialShipments: List<Shipment>,
    private var remoteResult: Result<Unit>
) : ShipmentRepository {

    private val localState = MutableStateFlow(initialShipments)

    override fun getShipmentsLocal(): Flow<List<Shipment>> = localState

    override suspend fun updateShipmentsRemote(): Result<Unit> = remoteResult

    override suspend fun getShipmentDetail(id: String): Result<ShipmentDetail> =
        Result.failure(IllegalStateException("not used in list tests"))
}