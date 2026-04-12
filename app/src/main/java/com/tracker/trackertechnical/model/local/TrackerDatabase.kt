package com.tracker.trackertechnical.model.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tracker.trackertechnical.model.local.shipment.ShipmentDao
import com.tracker.trackertechnical.model.local.shipment.ShipmentEntity

@Database(
    entities = [ShipmentEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TrackerDatabase : RoomDatabase() {
    abstract fun shipmentDao(): ShipmentDao
}