package ru.practicum.android.diploma.data.filter.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.dto.model.AreasDto
import ru.practicum.android.diploma.data.dto.model.CountriesDto
import ru.practicum.android.diploma.data.dto.model.FilterDto
import ru.practicum.android.diploma.data.dto.model.IndustriesDto
import ru.practicum.android.diploma.data.filter.AreaResponse
import ru.practicum.android.diploma.data.filter.AreasRequest
import ru.practicum.android.diploma.data.filter.FilterRepository
import ru.practicum.android.diploma.data.filter.IndustriasRequest
import ru.practicum.android.diploma.data.filter.IndustryResponse
import ru.practicum.android.diploma.data.network.ListResponseWrapper
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.data.shared.FilterLocalStorage

class FilterRepositoryImpl(
    private val retrofitNetworkClient: RetrofitNetworkClient,
    private val filterLocalStorage: FilterLocalStorage
) : FilterRepository {
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

    override suspend fun saveFilter(filterDto: FilterDto?) {
        filterLocalStorage.saveStorage(filterDto)
    }

    override suspend fun getFilter(): FilterDto? {
        return filterLocalStorage.getFromStorage()
    }

    override suspend fun savePlaceOfWork(countriesDto: CountriesDto, areasDto: AreasDto) {
        filterLocalStorage.savePlaceOfWork(countriesDto, areasDto)
    }

    override suspend fun saveIndustries(industriesDto: IndustriesDto) {
        filterLocalStorage.saveIndustries(industriesDto)
    }

    override suspend fun saveSalary(salaryDto: Int) {
        filterLocalStorage.saveSalary(salaryDto)
    }

    override suspend fun saveOnlyWithSalary(onlyWithSalary: Boolean) {
        filterLocalStorage.saveOnlyWithSalary(onlyWithSalary)
    }

    override suspend fun deletePlaceOfWork() {
        filterLocalStorage.deletePlaceOfWork()
    }

    override suspend fun deleteIndustries() {
        filterLocalStorage.deleteIndustries()
    }
}
