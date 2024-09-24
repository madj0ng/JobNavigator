package ru.practicum.android.diploma.domain.filters

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.CountryModel
import ru.practicum.android.diploma.domain.models.IndustryModel
import ru.practicum.android.diploma.domain.models.Resource

interface FilterInteractor {
    fun getAreas(): Flow<Resource<List<CountryModel>>>
    fun getIndustrias(): Flow<Resource<List<IndustryModel>>>
}
