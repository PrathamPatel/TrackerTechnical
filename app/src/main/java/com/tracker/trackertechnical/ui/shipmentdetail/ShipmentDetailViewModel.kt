package com.tracker.trackertechnical.ui.shipmentdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tracker.trackertechnical.repository.shipment.ShipmentRepository
import com.tracker.trackertechnical.ui.shipmentdetail.state.ShipmentDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShipmentDetailViewModel @Inject constructor(
    private val repository: ShipmentRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val  shipmentId: String = checkNotNull(savedStateHandle["shipmentId"])

    private val _state = MutableStateFlow<ShipmentDetailUiState>(ShipmentDetailUiState.Loading)
    val state = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _state.update { ShipmentDetailUiState.Loading }
            val result = repository.getShipmentDetail(shipmentId)
            result.fold(
                onSuccess = { detail ->
                    _state.update {
                        ShipmentDetailUiState.Content(detail)
                    }
                },
                onFailure = {
                    _state.update {
                        ShipmentDetailUiState.Error
                    }
                }
            )
        }
    }
}
