package ru.practicum.android.diploma.data.filter

import ru.practicum.android.diploma.data.dto.NetworkResponse

data class IndustryResponse(
    val industries: List<IndustryDto>,
) : NetworkResponse()

data class IndustryDto(
    val id: String,
    val name: String,
)
