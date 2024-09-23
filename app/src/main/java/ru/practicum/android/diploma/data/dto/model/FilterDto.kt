package ru.practicum.android.diploma.data.dto.model

class FilterDto(
    var country: CountriesDto? = null,
    var area: AreasDto? = null,
    var industries: IndustriesDto? = null,
    var salary: Int? = null,
    var onlyWithSalary: Boolean? = null,
)
