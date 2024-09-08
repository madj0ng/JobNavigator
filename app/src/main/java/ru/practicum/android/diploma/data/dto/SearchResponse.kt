package ru.practicum.android.diploma.data.dto

data class SearchResponse (
    var found: Int,
    var items: List<Vacancy>,
)
