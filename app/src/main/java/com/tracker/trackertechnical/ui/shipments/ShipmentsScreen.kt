package com.tracker.trackertechnical.ui.shipments

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.tracker.trackertechnical.R
import com.tracker.trackertechnical.constants.Constants
import com.tracker.trackertechnical.constants.Constants.PREVIEW_DATA
import com.tracker.trackertechnical.extensions.toMessage
import com.tracker.trackertechnical.ui.components.FullScreenMessage
import com.tracker.trackertechnical.ui.components.Loader
import com.tracker.trackertechnical.ui.shipments.components.ShipmentContentList
import com.tracker.trackertechnical.ui.shipments.components.ShipmentsTopBar
import com.tracker.trackertechnical.ui.shipments.intent.ShipmentIntent
import com.tracker.trackertechnical.ui.shipments.state.ShipmentContentState
import com.tracker.trackertechnical.ui.shipments.state.ShipmentState
import com.tracker.trackertechnical.ui.theme.TrackerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShipmentsScreen(
    state: ShipmentState,
    onIntent: (ShipmentIntent) -> Unit,
    onOpenShipment: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            ShipmentsTopBar(
                title = stringResource(R.string.shipments)
            ) {
                onIntent(ShipmentIntent.RefreshClicked)
            }
        }
    ) { paddingValues ->

        when (val content = state.contentState) {
            ShipmentContentState.Loading -> {
                Loader(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }

            is ShipmentContentState.Error -> {
                FullScreenMessage(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    title = R.string.something_went_wrong,
                    description = stringResource(content.errorType.toMessage()),
                    buttonText = stringResource(R.string.retry) ,
                    onButtonClick = { onIntent(ShipmentIntent.RetryClicked) }
                )
            }

            ShipmentContentState.Content -> {
                ShipmentContentList(
                    state = state,
                    paddingValues = paddingValues,
                    onRefresh = { onIntent(ShipmentIntent.RefreshClicked) },
                    onOpenShipment = onOpenShipment,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ShipmentsScreenLoadingPreview() {
    TrackerTheme {
        ShipmentsScreen(
            state = ShipmentState(
                isLoading = true,
                contentState = ShipmentContentState.Loading
            ),
            onIntent = {},
            onOpenShipment = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ShipmentsScreenContentPreview() {
    TrackerTheme {
        ShipmentsScreen(
            state = ShipmentState(
                isLoading = false,
                isRefreshing = false,
                shipments = PREVIEW_DATA,
                contentState = ShipmentContentState.Content
            ),
            onIntent = {},
            onOpenShipment = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ShipmentsScreenContentWithBannerPreview() {
    TrackerTheme {
        ShipmentsScreen(
            state = ShipmentState(
                isLoading = false,
                isRefreshing = false,
                shipments = PREVIEW_DATA,
                contentState = ShipmentContentState.Content,
                bannerMessage = R.string.refresh_failed_showing_cached_data
            ),
            onIntent = {},
            onOpenShipment = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ShipmentsScreenRefreshingPreview() {
    TrackerTheme {
        ShipmentsScreen(
            state = ShipmentState(
                isLoading = false,
                isRefreshing = true,
                shipments = PREVIEW_DATA,
                contentState = ShipmentContentState.Content
            ),
            onIntent = {},
            onOpenShipment = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ShipmentsScreenErrorPreview() {
    TrackerTheme {
        ShipmentsScreen(
            state = ShipmentState(
                isLoading = false,
                shipments = emptyList(),
                contentState = ShipmentContentState.Error(
                    errorType = Constants.ErrorType.EMPTY
                )
            ),
            onIntent = {},
            onOpenShipment = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ShipmentsScreenOfflineNoCachePreview() {
    TrackerTheme {
        ShipmentsScreen(
            state = ShipmentState(
                isLoading = false,
                shipments = emptyList(),
                contentState = ShipmentContentState.Error(
                    errorType = Constants.ErrorType.NO_CACHE
                )
            ),
            onIntent = {},
            onOpenShipment = {},
        )
    }
}