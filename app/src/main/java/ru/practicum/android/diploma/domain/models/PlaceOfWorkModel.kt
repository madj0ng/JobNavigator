package ru.practicum.android.diploma.domain.models

data class PlaceOfWorkModel(
    val country: CountryModel? = null,
    val region: RegionModel? = null,
    val city: CityModel? = null
)
