package ru.practicum.android.diploma.data.dto

class VacancySearchResponse(
    val items: List<VacancyDto>,
    val found: Int,
    val page: Int,
    val pages: Int
) : NetworkResponse()
