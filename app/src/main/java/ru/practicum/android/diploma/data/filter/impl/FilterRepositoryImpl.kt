package ru.practicum.android.diploma.data.filter.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.filter.AreaResponse
import ru.practicum.android.diploma.data.filter.AreasRequest
import ru.practicum.android.diploma.data.filter.FilterRepository
import ru.practicum.android.diploma.data.filter.IndustriasRequest
import ru.practicum.android.diploma.data.filter.IndustryResponse
import ru.practicum.android.diploma.data.network.ListResponseWrapper
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient

class FilterRepositoryImpl(private val retrofitNetworkClient: RetrofitNetworkClient) : FilterRepository {
    override fun getCountries(): Flow<ListResponseWrapper<AreaResponse>> = flow {
        val response = retrofitNetworkClient.doRequest(AreasRequest())
        val result: ListResponseWrapper<AreaResponse>
        if (response.resultCode == RetrofitNetworkClient.RESULT_CODE_SUCCESS) {
            result = response as ListResponseWrapper<AreaResponse>
        } else {
            result = ListResponseWrapper<AreaResponse>().apply { resultCode = response.resultCode }
        }
        emit(
            result
        )
    }

    override fun getIndustries(): Flow<ListResponseWrapper<IndustryResponse>> = flow {
        val response = retrofitNetworkClient.doRequest(IndustriasRequest())
        val result: ListResponseWrapper<IndustryResponse>
        if (response.resultCode == RetrofitNetworkClient.RESULT_CODE_SUCCESS) {
            result = response as ListResponseWrapper<IndustryResponse>
        } else {
            result = ListResponseWrapper<IndustryResponse>().apply { resultCode = response.resultCode }
        }
        emit(
            result
        )
    }
}
