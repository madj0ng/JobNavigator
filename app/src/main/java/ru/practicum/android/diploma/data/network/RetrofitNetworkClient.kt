package ru.practicum.android.diploma.data.network

import android.content.Context
import androidx.annotation.StringRes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.data.dto.NetworkResponse
import ru.practicum.android.diploma.data.dto.VacancySearchRequest
import ru.practicum.android.diploma.data.dto.VacancySearchResponse
import ru.practicum.android.diploma.data.dto.vacancydetail.VacancyDetailsRequest
import ru.practicum.android.diploma.data.dto.vacancydetail.VacancyDetailsResponse
import ru.practicum.android.diploma.data.filter.AreasRequest
import ru.practicum.android.diploma.data.filter.IndustriasRequest
import ru.practicum.android.diploma.util.Connected
import java.io.IOException

class RetrofitNetworkClient(
    private val context: Context,
    private val hhApi: HHApiService,
    private val connected: Connected,
) : NetworkClient {

    fun setCondition (dto: Any): Boolean {
        return (dto !is VacancySearchRequest
            && dto !is VacancyDetailsRequest
            && dto !is AreasRequest
            && dto !is IndustriasRequest
            )
    }

    override suspend fun doRequest(dto: Any): NetworkResponse {
        if (setCondition(dto)) {
            return errorResponse(ERROR_CODE_BAD_REQUEST, R.string.search_error_server)

        }

        return withContext(Dispatchers.IO) {
            if (connected.isConnected()) {
                try {
                    when (dto) {
                        is VacancySearchRequest -> vacancySearchRequest(dto)
                        is IndustriasRequest -> {
                            val response = hhApi.getIndustries()
                            ListResponseWrapper(response).apply { resultCode = RESULT_CODE_SUCCESS }
                        }

                        is AreasRequest -> {
                            val response = hhApi.getAreas()
                            ListResponseWrapper(response).apply { resultCode = RESULT_CODE_SUCCESS }
                        }

                        is VacancyDetailsRequest -> vacancyDetailsRequest(dto)

                        else -> {
                            errorResponse(ERROR_CODE_SERVER, R.string.search_error_server)
                        }
                    }
                } catch (e: IOException) {
                    errorResponse(ERROR_CODE_SERVER, R.string.search_error_server)
                    throw e
                }
            } else {
                errorResponse(ERROR_CODE_INTERNET, R.string.search_error_no_connect)
            }

        }
    }

    private suspend fun vacancySearchRequest(vacancySearchRequest: VacancySearchRequest): VacancySearchResponse {
        return hhApi.getVacancies(vacancySearchRequest.queryOptions).apply { resultCode = RESULT_CODE_SUCCESS }
    }

    private suspend fun vacancyDetailsRequest(vacancyDetailsRequest: VacancyDetailsRequest): NetworkResponse {
        return VacancyDetailsResponse(hhApi.getVacancy(vacancyDetailsRequest.vacancyId)).apply {
            resultCode = RESULT_CODE_SUCCESS
        }
    }

    private fun errorResponse(errCode: Int, @StringRes mesInt: Int): NetworkResponse {
        return NetworkResponse().apply {
            resultCode = errCode
            message = context.getString(mesInt)
        }
    }

    companion object {
        const val RESULT_CODE_SUCCESS = 200
        const val ERROR_CODE_INTERNET = -1
        const val ERROR_CODE_SERVER = 500
        const val ERROR_CODE_BAD_REQUEST = 400
    }
}
