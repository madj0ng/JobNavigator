package ru.practicum.android.diploma.presentation.mapper

import ru.practicum.android.diploma.domain.models.AreaFilterModel
import ru.practicum.android.diploma.domain.models.CityModel
import ru.practicum.android.diploma.domain.models.CountryFilterModel
import ru.practicum.android.diploma.domain.models.CountryModel
import ru.practicum.android.diploma.domain.models.IndustriesFilterModel
import ru.practicum.android.diploma.domain.models.IndustryModel
import ru.practicum.android.diploma.domain.models.RegionModel

class MapperFilter {
    fun map(industry: IndustryModel): IndustriesFilterModel {
        return IndustriesFilterModel(industry.id, industry.name)
    }

    fun map(industry: IndustriesFilterModel): IndustryModel {
        return IndustryModel(industry.id, industry.name)
    }

    fun map(country: CountryModel): CountryFilterModel {
        return CountryFilterModel(country.id, country.name)
    }

    fun mapCity(city: CityModel): AreaFilterModel {
        return AreaFilterModel(city.id, city.name)
    }
    fun mapRegion(region: RegionModel): AreaFilterModel {
        return AreaFilterModel(region.id, region.name)
    }
}
