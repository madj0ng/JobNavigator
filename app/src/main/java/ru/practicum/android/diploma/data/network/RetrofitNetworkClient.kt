package ru.practicum.android.diploma.data.network

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.data.dto.NetworkResponse
import ru.practicum.android.diploma.data.dto.VacancySearchRequest
import ru.practicum.android.diploma.util.Connected

class RetrofitNetworkClient(
    private val context: Context,
    private val hhApi: HhApi,
    private val connected: Connected
) : NetworkClient {

    override suspend fun doRequest(dto: Any): NetworkResponse {
        if (dto !is VacancySearchRequest) {
            return NetworkResponse().apply {
                resultCode = 400
                message = context.getString(R.string.search_error_server)
            }
        }

        if (connected.isConnected()) {
            return withContext(Dispatchers.IO) {
                try {
                    val response = hhApi.search(dto.request, BuildConfig.HH_ACCESS_TOKEN)
                    response.apply { resultCode = 200 }
                } catch (e: Exception) {
                    NetworkResponse().apply {
                        resultCode = 500
                        message = context.getString(R.string.search_error_server)
                    }
                }
            }
        } else {
            return NetworkResponse().apply {
                resultCode = -1
                message = context.getString(R.string.search_connection_faild)
            }
        }
    }
}
