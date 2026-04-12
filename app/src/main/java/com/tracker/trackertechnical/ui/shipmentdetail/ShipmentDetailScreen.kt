package com.tracker.trackertechnical.ui.shipmentdetail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.tracker.trackertechnical.R
import com.tracker.trackertechnical.constants.Constants.PREVIEW_SHIPMENT_DETAIL
import com.tracker.trackertechnical.ui.components.FullScreenMessage
import com.tracker.trackertechnical.ui.components.Loader
import com.tracker.trackertechnical.ui.shipmentdetail.components.ShipmentDetailBody
import com.tracker.trackertechnical.ui.shipmentdetail.state.ShipmentDetailUiState
import com.tracker.trackertechnical.ui.theme.TrackerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShipmentDetailScreen(
    state: ShipmentDetailUiState,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.shipment_detail_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
            )
        },
    ) { paddingValues ->
        when (state) {
            ShipmentDetailUiState.Loading -> {
                Loader(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                )
            }

            ShipmentDetailUiState.Error -> {
                FullScreenMessage(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    title = R.string.something_went_wrong,
                    description = stringResource(R.string.shipment_detail_load_error),
                    buttonText = stringResource(R.string.retry),
                    onButtonClick = onRetry,
                )
            }

            is ShipmentDetailUiState.Content -> {
                ShipmentDetailBody(
                    detail = state.detail,
                    paddingValues = paddingValues,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ShipmentDetailScreenErrorPreview() {
    TrackerTheme {
        ShipmentDetailScreen(
            state = ShipmentDetailUiState.Error,
            onBack = {},
            onRetry = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ShipmentDetailScreenContentPreview() {
    TrackerTheme {
        ShipmentDetailScreen(
            state = ShipmentDetailUiState.Content(PREVIEW_SHIPMENT_DETAIL),
            onBack = {},
            onRetry = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ShipmentDetailScreenPartialTimelinePreview() {
    TrackerTheme {
        ShipmentDetailScreen(
            state = ShipmentDetailUiState.Content(
                PREVIEW_SHIPMENT_DETAIL.copy(isTimelinePartial = true),
            ),
            onBack = {},
            onRetry = {},
        )
    }
}