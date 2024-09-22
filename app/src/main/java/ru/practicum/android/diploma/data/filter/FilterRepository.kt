package ru.practicum.android.diploma.data.filter

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.dto.model.FilterDto
import ru.practicum.android.diploma.data.network.ListResponseWrapper

interface FilterRepository {

    fun getCountries(): Flow<ListResponseWrapper<AreaResponse>>
    fun getIndustries(): Flow<ListResponseWrapper<IndustryResponse>>
    suspend fun getFilter(): FilterDto?
    suspend fun saveFilter(filterDto: FilterDto?)
}
