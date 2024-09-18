package ru.practicum.android.diploma.data.dto

class VacancySearchResponse(
    val items: List<VacancyDto>,
    val found: Int
) : NetworkResponse()
