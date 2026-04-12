package com.tracker.trackertechnical.ui.shipmentdetail.state

import com.tracker.trackertechnical.model.local.shipment.ShipmentDetail

sealed interface ShipmentDetailUiState {
    data object Loading : ShipmentDetailUiState
    data class Content(val detail: ShipmentDetail) : ShipmentDetailUiState
    data object Error : ShipmentDetailUiState
}
