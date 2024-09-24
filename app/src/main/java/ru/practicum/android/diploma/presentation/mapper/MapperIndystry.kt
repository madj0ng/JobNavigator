package ru.practicum.android.diploma.presentation.mapper

import ru.practicum.android.diploma.domain.models.IndustriesFilterModel
import ru.practicum.android.diploma.domain.models.IndustryModel

class MapperIndystry {
    fun map(industry: IndustryModel): IndustriesFilterModel {
        return IndustriesFilterModel(industry.id, industry.name)
    }

    fun map(industry: IndustriesFilterModel): IndustryModel {
        return IndustryModel(industry.id, industry.name)
    }
}
