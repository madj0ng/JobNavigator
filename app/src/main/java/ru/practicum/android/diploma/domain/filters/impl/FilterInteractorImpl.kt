package ru.practicum.android.diploma.domain.filters.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.data.filter.FilterRepository
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.domain.filters.FilterInteractor
import ru.practicum.android.diploma.domain.models.CountryModel
import ru.practicum.android.diploma.domain.models.IndustryModel
import ru.practicum.android.diploma.domain.models.RegionModel
import ru.practicum.android.diploma.domain.models.Resource

class FilterInteractorImpl(val repository: FilterRepository) : FilterInteractor {
    override fun getAreas(): Flow<Resource<List<CountryModel>>> {

        return repository.getCountries().map { countryList ->
            if (countryList.resultCode == RetrofitNetworkClient.RESULT_CODE_SUCCESS) {
                Resource.Success(countryList.list.map { country ->
                    CountryModel(country.name, country.id, country.regions.map { region ->
                        RegionModel(region.id, region.name)
                    })
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
                Resource.Success(industryResultList)
            } else {
                Resource.Error(-1, "")
            }
        }

    }
}
