package com.tracker.trackertechnical.navigation.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tracker.trackertechnical.ui.shipmentdetail.ShipmentDetailScreen
import com.tracker.trackertechnical.ui.shipmentdetail.ShipmentDetailViewModel

@Composable
fun ShipmentDetailRoute(
    onBack: () -> Unit,
    viewModel: ShipmentDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ShipmentDetailScreen(
        state = state,
        onBack = onBack,
        onRetry = viewModel::load,
    )
}
