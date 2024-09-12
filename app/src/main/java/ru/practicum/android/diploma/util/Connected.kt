package ru.practicum.android.diploma.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class Connected(private val context: Context) {

    fun isConnected(): Boolean {
        var connectionStatus = false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> connectionStatus = true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> connectionStatus = true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> connectionStatus = true
            }
        }
        return connectionStatus
    }
}
