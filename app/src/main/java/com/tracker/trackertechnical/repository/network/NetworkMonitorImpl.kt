package com.tracker.trackertechnical.repository.network

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission
import javax.inject.Inject

class NetworkMonitorImpl @Inject constructor(
    private val context: Context
) : NetworkMonitor {

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    override fun isOnline(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}