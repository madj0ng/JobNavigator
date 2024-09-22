package ru.practicum.android.diploma.domain.models

data class FilterModel(
    val country: CountryFilterModel? = null,
    val area: AreaFilterModel? = null,
    val industries: IndustriesFilterModel? = null,
    val salary: Int? = null,
    val onlyWithSalary: Boolean? = null,
)
