package ru.practicum.android.diploma.presentation.models

import ru.practicum.android.diploma.domain.models.CityModel
import ru.practicum.android.diploma.domain.models.CountryModel
import ru.practicum.android.diploma.domain.models.RegionModel

data class PlaceOfWorkModel(
    val countryModel: CountryModel? = null,
    val regionModel: RegionModel? = null,
    val cityModel: CityModel? = null
)
