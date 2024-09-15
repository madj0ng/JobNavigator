package ru.practicum.android.diploma.presentation.models

import ru.practicum.android.diploma.domain.models.VacancyDetailsModel

sealed interface VacancyDetailsScreenState{
    data object Loading : VacancyDetailsScreenState

    data class Content(val data: VacancyDetailsModel) : VacancyDetailsScreenState

    data object ErrorNoInternet : VacancyDetailsScreenState

    data object ErrorServer : VacancyDetailsScreenState

    data object NotFound : VacancyDetailsScreenState
}
