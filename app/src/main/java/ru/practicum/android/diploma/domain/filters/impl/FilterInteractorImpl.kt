package ru.practicum.android.diploma.domain.filters.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.data.filter.FilterRepository
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.domain.filters.FilterInteractor
import ru.practicum.android.diploma.domain.models.AreaFilterModel
import ru.practicum.android.diploma.domain.models.CityModel
import ru.practicum.android.diploma.domain.models.CountryFilterModel
import ru.practicum.android.diploma.domain.models.CountryModel
import ru.practicum.android.diploma.domain.models.FilterModel
import ru.practicum.android.diploma.domain.models.IndustriesFilterModel
import ru.practicum.android.diploma.domain.models.IndustryModel
import ru.practicum.android.diploma.domain.models.RegionModel
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.util.FilterConverter

class FilterInteractorImpl(
    private val repository: FilterRepository,
    private val converter: FilterConverter
) : FilterInteractor {
    override fun getAreas(): Flow<Resource<List<CountryModel>>> {
        return repository.getCountries().map { countryList ->
            if (countryList.resultCode == RetrofitNetworkClient.RESULT_CODE_SUCCESS) {
                Resource.Success(data = countryList.list.map { country ->
                    CountryModel(
                        country.name,
                        country.id,
                        country.regions.map { region ->
                            RegionModel(
                                region.id,
                                region.name,
                                region.city.map { city ->
                                    CityModel(city.id, city.name)
                                }
                            )
                        }
                    )
                })
            } else {
                Resource.Error(-1, "")
            }
        }
    }

    override fun getIndustrias(): Flow<Resource<List<IndustryModel>>> {
        return repository.getIndustries().map { industryList ->
            if (industryList.resultCode == RetrofitNetworkClient.RESULT_CODE_SUCCESS) {
                val industryResultList = mutableListOf<IndustryModel>()
                for (industrySector in industryList.list) {
                    for (subIndustry in industrySector.industries) {
                        industryResultList.add(IndustryModel(subIndustry.id, subIndustry.name))
                    }
                }
                Resource.Success(data = industryResultList)
            } else {
                Resource.Error(-1, "")
            }
        }

    }

    override suspend fun saveFilter(filterModel: FilterModel?) {
        if (filterModel != null) {
            repository.saveFilter(converter.map(filterModel))
        } else {
            repository.saveFilter(null)
        }
    }

    override suspend fun getFilter(): Flow<FilterModel?> {
        return repository.getFilter().map { filter ->
            if (filter != null) {
                converter.map(filter)
            } else {
                null
            }
        }
    }

    override suspend fun savePlaceOfWork(countryFilterModel: CountryFilterModel, areaFilterModel: AreaFilterModel) {
        repository.savePlaceOfWork(converter.mapCountry(countryFilterModel), converter.mapArea(areaFilterModel))
    }

    override suspend fun saveIndustries(industriesFilterModel: IndustriesFilterModel) {
        repository.saveIndustries(converter.mapIndustries(industriesFilterModel))
    }

    override suspend fun saveSalary(salary: Int) {
        repository.saveSalary(salary)
    }

    override suspend fun saveOnlyWithSalary(onlyWithSalary: Boolean) {
        repository.saveOnlyWithSalary(onlyWithSalary)
    }

    override suspend fun deletePlaceOfWork() {
        repository.deletePlaceOfWork()
    }

    override suspend fun deleteIndustries() {
        repository.deleteIndustries()
    }

    override suspend fun saveCheckSalary(onlyWithSalary: Boolean) {
        repository.saveCheckSalary(onlyWithSalary)
    }
}
