package com.tracker.trackertechnical.ui.shipments.state

import com.tracker.trackertechnical.constants.Constants
import com.tracker.trackertechnical.ui.shipments.model.ShipmentItem

data class ShipmentState(
    val isLoading : Boolean = false,
    val isRefreshing : Boolean = false,
    val shipments : List<ShipmentItem> = emptyList(),
    val contentState : ShipmentContentState = ShipmentContentState.Loading,
    val bannerMessage : Int? = null
)

sealed interface ShipmentContentState{
    data object Loading : ShipmentContentState
    data object Content : ShipmentContentState
    data class Error(val errorType : Constants.ErrorType) : ShipmentContentState
}
