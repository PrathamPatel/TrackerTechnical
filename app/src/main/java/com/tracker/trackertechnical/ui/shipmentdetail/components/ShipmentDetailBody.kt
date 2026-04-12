package com.tracker.trackertechnical.ui.shipmentdetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tracker.trackertechnical.R
import com.tracker.trackertechnical.extensions.formatIsoUtcDate
import com.tracker.trackertechnical.model.local.shipment.ShipmentDetail
import com.tracker.trackertechnical.ui.components.InfoBanner
import com.tracker.trackertechnical.util.LocationUtil.formatLocation

@Composable
fun ShipmentDetailBody(
    detail: ShipmentDetail,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.extraLarge,
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp),
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    Text(
                        text = detail.carrierName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = detail.carrierCode,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    HorizontalDivider()
                    DetailLabeledRow(
                        label = stringResource(R.string.tracking_number_label),
                        value = detail.trackingNumber,
                    )
                    formatLocation(detail.originCity, detail.originCountry)?.let { origin ->
                        DetailLabeledRow(
                            label = stringResource(R.string.origin_label),
                            value = origin,
                        )
                    }
                    formatLocation(detail.destinationCity, detail.destinationCountry)?.let { dest ->
                        DetailLabeledRow(
                            label = stringResource(R.string.destination_label),
                            value = dest,
                        )
                    }
                    detail.estimatedDeliveryAt?.let { eta ->
                        DetailLabeledRow(
                            label = stringResource(R.string.estimated_delivery_label),
                            value = eta.formatIsoUtcDate(),
                        )
                    }
                }
            }
        }

        if (detail.isTimelinePartial) {
            item {
                InfoBanner(message = stringResource(R.string.shipment_detail_partial_timeline))
            }
        }

        item {
            Text(
                text = stringResource(R.string.status_history_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
        }

        if (detail.statuses.isEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.status_history_empty),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        } else {
            items(
                items = detail.statuses,
                key = { "${it.timeIso}_${it.code}" },
            ) { event ->
                TimelineStatusCard(event = event)
            }
        }
    }
}