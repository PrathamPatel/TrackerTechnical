package com.tracker.trackertechnical.model.local.shipment

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shipments")
data class ShipmentEntity(
    @PrimaryKey val id: String,
    val carrierCode: String,
    val carrierName: String,
    val trackingNumber: String,
    val statusCode: String,
    val statusLabel: String,
    val lastUpdatedAt: String,
    val originCity: String?,
    val originCountry: String?,
    val destinationCity: String?,
    val destinationCountry: String?,
    val estimatedDeliveryAt: String?
)
