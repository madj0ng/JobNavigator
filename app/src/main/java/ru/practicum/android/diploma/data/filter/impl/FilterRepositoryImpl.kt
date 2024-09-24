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

class FilterRepositoryImpl(val retrofitNetworkClient: RetrofitNetworkClient) : FilterRepository {
    override fun getCountries(): Flow<ListResponseWrapper<AreaResponse>> = flow {
        emit(
            retrofitNetworkClient.doRequest(AreasRequest()) as ListResponseWrapper<AreaResponse>
        )
    }

    override fun getIndustries(): Flow<ListResponseWrapper<IndustryResponse>> = flow {
        emit(
            retrofitNetworkClient.doRequest(IndustriasRequest()) as ListResponseWrapper<IndustryResponse>
        )
    }
}
