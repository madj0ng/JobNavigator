package ru.practicum.android.diploma.presentation.models

sealed interface JobSearchScreenState {
    data object Loading : JobSearchScreenState

    data class Content(val data: List<VacancyInfo>) : JobSearchScreenState

    data object ErrorNoInternet : JobSearchScreenState

    data object ErrorServer : JobSearchScreenState

    data object Empty : JobSearchScreenState
}
