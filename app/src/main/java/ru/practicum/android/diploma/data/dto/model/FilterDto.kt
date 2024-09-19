package ru.practicum.android.diploma.data.dto.model

data class FilterDto(
    val country: CountriesDto? = null,
    val area: AreasDto? = null,
    val industries: IndustriesDto? = null,
    val salary: Int? = null,
    val only_with_salary: Boolean? = null,
)
