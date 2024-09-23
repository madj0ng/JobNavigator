package ru.practicum.android.diploma.domain.models

data class FilterModel(
    var country: CountryFilterModel? = null,
    var area: AreaFilterModel? = null,
    var industries: IndustriesFilterModel? = null,
    var salary: Int? = null,
    var onlyWithSalary: Boolean? = null,
)
