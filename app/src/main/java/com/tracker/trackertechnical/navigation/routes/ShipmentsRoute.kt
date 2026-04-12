package com.tracker.trackertechnical.navigation.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tracker.trackertechnical.ui.shipments.ShipmentsScreen
import com.tracker.trackertechnical.ui.shipments.ShipmentsViewModel

@Composable
fun ShipmentsRoute(
    onOpenShipment: (String) -> Unit,
    viewModel: ShipmentsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ShipmentsScreen(
        state = state,
        onIntent = viewModel::handleIntent,
        onOpenShipment = onOpenShipment,
    )
}