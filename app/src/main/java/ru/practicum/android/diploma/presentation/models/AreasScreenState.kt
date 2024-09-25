package ru.practicum.android.diploma.presentation.models

import ru.practicum.android.diploma.domain.models.CountryModel

sealed interface AreasScreenState {
    data class Content(val data: List<CountryModel>) : AreasScreenState

    data object ErrorInternet : AreasScreenState

    data object Error : AreasScreenState

}
