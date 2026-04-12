package com.tracker.trackertechnical.di

import android.content.Context
import androidx.room.Room
import com.tracker.trackertechnical.model.local.TrackerDatabase
import com.tracker.trackertechnical.model.local.shipment.ShipmentDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideTrackerDatabase(
        @ApplicationContext context: Context
    ): TrackerDatabase {
        return Room.databaseBuilder(
            context,
            TrackerDatabase::class.java,
            "tracker_database"
        ).build()
    }

    @Provides
    fun provideShipmentDao(
        database: TrackerDatabase
    ): ShipmentDao {
        return database.shipmentDao()
    }
}