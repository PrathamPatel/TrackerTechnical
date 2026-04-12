package com.tracker.trackertechnical.di

import com.tracker.trackertechnical.repository.network.NetworkMonitor
import com.tracker.trackertechnical.repository.network.NetworkMonitorImpl
import com.tracker.trackertechnical.repository.shipment.ShipmentRepository
import com.tracker.trackertechnical.repository.shipment.ShipmentRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindShipmentRepository(
        shipmentRepositoryImpl: ShipmentRepositoryImpl
    ): ShipmentRepository

    @Binds
    @Singleton
    abstract fun bindNetworkMonitor(
        impl: NetworkMonitorImpl
    ) : NetworkMonitor
}