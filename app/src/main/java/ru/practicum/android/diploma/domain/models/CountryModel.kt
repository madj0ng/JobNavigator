package ru.practicum.android.diploma.domain.models

data class CountryModel(
    val name: String,
    val id: String,
    val regions: List<RegionModel>,
)

data class RegionModel(
    val id: String,
    val name: String,
)
