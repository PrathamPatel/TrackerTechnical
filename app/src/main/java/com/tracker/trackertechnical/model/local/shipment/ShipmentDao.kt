package com.tracker.trackertechnical.model.local.shipment

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ShipmentDao {
    @Query("SELECT * FROM shipments ORDER BY id")
    fun getShipments(): Flow<List<ShipmentEntity>>

    @Query("SELECT * FROM shipments WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): ShipmentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ShipmentEntity>)

    @Query("DELETE FROM shipments")
    suspend fun clearAll()
}