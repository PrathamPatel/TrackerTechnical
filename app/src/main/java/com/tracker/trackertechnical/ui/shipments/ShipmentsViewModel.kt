package com.tracker.trackertechnical.ui.shipments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tracker.trackertechnical.R
import com.tracker.trackertechnical.constants.Constants
import com.tracker.trackertechnical.extensions.toUI
import com.tracker.trackertechnical.repository.network.NetworkMonitor
import com.tracker.trackertechnical.repository.shipment.ShipmentRepository
import com.tracker.trackertechnical.ui.shipments.intent.ShipmentIntent
import com.tracker.trackertechnical.ui.shipments.state.ShipmentContentState
import com.tracker.trackertechnical.ui.shipments.state.ShipmentState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ShipmentsViewModel @Inject constructor(
    private val shipmentRepository: ShipmentRepository,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _state = MutableStateFlow(
        ShipmentState(
            isLoading = true,
            contentState = ShipmentContentState.Loading
        )
    )
    val state: StateFlow<ShipmentState> = _state.asStateFlow()

    init {
        observeLocalShipments()
        handleIntent(ShipmentIntent.LoadInitial)
    }

    fun handleIntent(intent: ShipmentIntent) {
        when (intent) {
            ShipmentIntent.LoadInitial -> loadInitial()
            ShipmentIntent.RetryClicked -> retryLoad()
            ShipmentIntent.RefreshClicked -> refreshShipments()
        }
    }

    private fun observeLocalShipments() {
        viewModelScope.launch {
            shipmentRepository.getShipmentsLocal().collect { shipments ->
                val uiItems = shipments.map { it.toUI() }

                _state.update { current ->
                    when {
                        uiItems.isNotEmpty() -> {
                            current.copy(
                                shipments = uiItems,
                                contentState = ShipmentContentState.Content
                            )
                        }

                        current.isLoading -> {
                            current.copy(
                                shipments = emptyList(),
                                contentState = ShipmentContentState.Loading
                            )
                        }

                        else -> {
                            current.copy(
                                shipments = emptyList()
                            )
                        }
                    }
                }
            }
        }
    }

    private fun loadInitial() {
        viewModelScope.launch {
            val hasCache = _state.value.shipments.isNotEmpty()

            _state.update { current ->
                current.copy(
                    isLoading = !hasCache,
                    isRefreshing = hasCache,
                    bannerMessage = null,
                    contentState = if (hasCache) {
                        ShipmentContentState.Content
                    } else {
                        ShipmentContentState.Loading
                    }
                )
            }

            handleSyncResult(
                result = syncShipments(),
                errorType = Constants.ErrorType.GENERAL
            )
        }
    }

    private fun retryLoad() {
        viewModelScope.launch {
            _state.update { current ->
                current.copy(
                    isLoading = true,
                    isRefreshing = false,
                    bannerMessage = null,
                    contentState = ShipmentContentState.Loading
                )
            }

            handleSyncResult(
                result = syncShipments(),
                errorType = Constants.ErrorType.GENERAL
            )
        }
    }

    private fun refreshShipments() {
        viewModelScope.launch {
            _state.update { current ->
                current.copy(
                    isRefreshing = true,
                    bannerMessage = null
                )
            }

            handleSyncResult(
                result = syncShipments(),
                errorType = Constants.ErrorType.FAILED_REFRESH
            )
        }
    }

    private suspend fun syncShipments(): Result<Unit> {
        return shipmentRepository.updateShipmentsRemote()
    }

    private suspend fun handleSyncResult(
        result: Result<Unit>,
        errorType: Constants.ErrorType
    ) {
        val isOnline = networkMonitor.isOnline()
        val cached = shipmentRepository.getShipmentsLocal().first()

        result.fold(
            onSuccess = {
                Timber.d("Shipments sync success")

                _state.update { current ->
                    current.copy(
                        isLoading = false,
                        isRefreshing = false,
                        contentState = if (cached.isEmpty()) {
                            ShipmentContentState.Error(errorType = Constants.ErrorType.EMPTY)
                        } else {
                            ShipmentContentState.Content
                        }
                    )
                }
            },
            onFailure = { throwable ->
                Timber.e(throwable, "Shipments sync failed")

                _state.update { current ->
                    when {
                        cached.isNotEmpty() -> {
                            current.copy(
                                isLoading = false,
                                isRefreshing = false,
                                contentState = ShipmentContentState.Content,
                                bannerMessage = when {
                                    isOnline -> {
                                        R.string.could_not_refresh_showing_saved_data
                                    }
                                    else -> {
                                        R.string.offline_showing_saved_data
                                    }
                                }
                            )
                        }

                        !isOnline -> {
                            current.copy(
                                isLoading = false,
                                isRefreshing = false,
                                contentState = ShipmentContentState.Error(errorType = Constants.ErrorType.NO_CACHE)
                            )
                        }

                        else -> {
                            current.copy(
                                isLoading = false,
                                isRefreshing = false,
                                contentState = ShipmentContentState.Error(errorType = errorType)
                            )
                        }
                    }
                }
            }
        )
    }
}