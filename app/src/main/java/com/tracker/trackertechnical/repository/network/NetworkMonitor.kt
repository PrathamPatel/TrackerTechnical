package com.tracker.trackertechnical.repository.network

interface NetworkMonitor {
    fun isOnline() : Boolean
}