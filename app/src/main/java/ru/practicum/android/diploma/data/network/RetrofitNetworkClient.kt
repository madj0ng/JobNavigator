package ru.practicum.android.diploma.data.network

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.data.dto.NetworkResponse
import ru.practicum.android.diploma.data.dto.VacancySearchRequest
import ru.practicum.android.diploma.util.Connected
import java.io.IOException

class RetrofitNetworkClient(
    private val context: Context,
    private val hhApi: HHApiService,
    private val connected: Connected
) : NetworkClient {

    override suspend fun doRequest(dto: Any): NetworkResponse {
        if (dto !is VacancySearchRequest) {
            return NetworkResponse().apply {
                resultCode = ERROR_CODE_BAD_REQUEST
                message = context.getString(R.string.search_error_server)
            }
        }

        return withContext(Dispatchers.IO) {
            if (connected.isConnected()) {
                try {
                    val response = hhApi.getVacancies(dto.request)
                    response.apply { resultCode = RESULT_CODE_SUCCESS }
                } catch (e: IOException) {
                    NetworkResponse().apply {
                        resultCode = ERROR_CODE_SERVER
                        message = context.getString(R.string.search_error_server)
                    }
                    throw NetworkExeption("Network error occurred", e)
                }
            } else {
                NetworkResponse().apply {
                    resultCode = ERROR_CODE_INTERNET
                    message = context.getString(R.string.search_connection_faild)
                }
            }

        }
    }

    companion object {
        const val RESULT_CODE_SUCCESS = 200
        const val ERROR_CODE_INTERNET = -1
        const val ERROR_CODE_SERVER = 500
        const val ERROR_CODE_BAD_REQUEST = 400
    }
}
