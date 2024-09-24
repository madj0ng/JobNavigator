package ru.practicum.android.diploma.domain.filters

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.AreaFilterModel
import ru.practicum.android.diploma.domain.models.CountryFilterModel
import ru.practicum.android.diploma.domain.models.CountryModel
import ru.practicum.android.diploma.domain.models.FilterModel
import ru.practicum.android.diploma.domain.models.IndustriesFilterModel
import ru.practicum.android.diploma.domain.models.IndustryModel
import ru.practicum.android.diploma.domain.models.Resource

interface FilterInteractor {
    fun getAreas(): Flow<Resource<List<CountryModel>>>
    fun getIndustrias(): Flow<Resource<List<IndustryModel>>>
    suspend fun getFilter(): Flow<FilterModel?>
    suspend fun saveFilter(filterModel: FilterModel?)
    suspend fun savePlaceOfWork(countryFilterModel: CountryFilterModel?, areaFilterModel: AreaFilterModel?)
    suspend fun saveIndustries(industriesFilterModel: IndustriesFilterModel)
    suspend fun saveSalary(salary: Int)
    suspend fun saveOnlyWithSalary(onlyWithSalary: Boolean)
    suspend fun deletePlaceOfWork()
    suspend fun deleteIndustries()
}
