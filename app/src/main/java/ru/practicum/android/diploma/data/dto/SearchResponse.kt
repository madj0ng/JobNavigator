package ru.practicum.android.diploma.data.dto

data class SearchResponse(
    val found: Int,
    val items: List<Vacancy>,
)
