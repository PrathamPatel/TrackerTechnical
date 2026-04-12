package com.tracker.trackertechnical.ui.shipments.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tracker.trackertechnical.ui.components.InfoBanner
import com.tracker.trackertechnical.ui.shipments.state.ShipmentState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShipmentContentList(
    state: ShipmentState,
    paddingValues: PaddingValues,
    onRefresh: () -> Unit,
    onOpenShipment: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        state.bannerMessage?.let { message ->
            InfoBanner(
                message = stringResource(message),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 12.dp,
                        bottom = 8.dp
                    )
            )
        }

        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = onRefresh,
            modifier = Modifier
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 4.dp,
                    bottom = 24.dp
                ),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(
                    items = state.shipments,
                    key = { it.id }
                ) { shipment ->
                    ShipmentCard(
                        shipment = shipment,
                        onClick = { onOpenShipment(shipment.id) },
                    )
                }
            }
        }
    }
}