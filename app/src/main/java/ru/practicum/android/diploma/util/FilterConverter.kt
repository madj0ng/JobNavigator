package ru.practicum.android.diploma.util

import ru.practicum.android.diploma.data.dto.model.AreasDto
import ru.practicum.android.diploma.data.dto.model.CountriesDto
import ru.practicum.android.diploma.data.dto.model.FilterDto
import ru.practicum.android.diploma.data.dto.model.IndustriesDto
import ru.practicum.android.diploma.domain.models.AreaFilterModel
import ru.practicum.android.diploma.domain.models.CountryFilterModel
import ru.practicum.android.diploma.domain.models.FilterModel
import ru.practicum.android.diploma.domain.models.IndustriesFilterModel

class FilterConverter {
    fun map(filterModel: FilterModel): FilterDto {
        return FilterDto(
            CountriesDto(filterModel.country?.id ?: "", filterModel.country?.name ?: ""),
            AreasDto(filterModel.area?.id ?: "", filterModel.area?.name ?: ""),
            IndustriesDto(filterModel.industries?.id ?: "", filterModel.industries?.name ?: ""),
            filterModel.salary,
            filterModel.onlyWithSalary
        )
    }

    fun map(filterDto: FilterDto): FilterModel {
        return FilterModel(
            CountryFilterModel(filterDto.country?.id ?: "", filterDto.country?.name ?: ""),
            AreaFilterModel(filterDto.area?.id ?: "", filterDto.area?.name ?: ""),
            IndustriesFilterModel(filterDto.industries?.id ?: "", filterDto.industries?.name ?: ""),
            filterDto.salary,
            filterDto.onlyWithSalary
        )
    }
}
