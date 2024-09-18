package ru.practicum.android.diploma.data.network

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.data.dto.NetworkResponse
import ru.practicum.android.diploma.data.dto.VacancySearchRequest
import ru.practicum.android.diploma.data.dto.vacancydetail.VacancyDetailsRequest
import ru.practicum.android.diploma.data.dto.vacancydetail.VacancyDetailsResponse
import ru.practicum.android.diploma.util.Connected
import java.io.IOException

class RetrofitNetworkClient(
    private val context: Context,
    private val hhApi: HHApiService,
    private val connected: Connected,
) : NetworkClient {

    override suspend fun doRequest(dto: Any): NetworkResponse {
        if (dto !is VacancySearchRequest && dto !is VacancyDetailsRequest) {
            return NetworkResponse().apply {
                resultCode = ERROR_CODE_BAD_REQUEST
                message = context.getString(R.string.search_error_server)
            }
        }

        return withContext(Dispatchers.IO) {
            if (connected.isConnected()) {
                try {
                    when (dto) {
                        is VacancySearchRequest -> {
                            val response = hhApi.getVacancies(
                                dto.queryOptions
                            )
                            response.apply { resultCode = RESULT_CODE_SUCCESS }
                        }

                        is VacancyDetailsRequest -> {
                            val responseDto = hhApi.getVacancy(dto.vacancyId)
                            val response = VacancyDetailsResponse(responseDto)
                            response.apply { resultCode = RESULT_CODE_SUCCESS }
                        }

                        else -> {
                            NetworkResponse().apply {
                                resultCode = ERROR_CODE_SERVER
                                message = context.getString(R.string.search_error_server)
                            }
                        }
                    }
                } catch (e: IOException) {
                    NetworkResponse().apply {
                        resultCode = ERROR_CODE_SERVER
                        message = context.getString(R.string.search_error_server)
                    }
                    throw e
                }
            } else {
                NetworkResponse().apply {
                    resultCode = ERROR_CODE_INTERNET
                    message = context.getString(R.string.search_error_no_connect)
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
