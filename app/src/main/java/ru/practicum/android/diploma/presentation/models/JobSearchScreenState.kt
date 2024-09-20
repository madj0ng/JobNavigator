package ru.practicum.android.diploma.presentation.models

sealed interface JobSearchScreenState {
    data object Loading : JobSearchScreenState

    data class Content(val data: List<VacancyInfo>, val found: Int) : JobSearchScreenState

    data object ErrorNoInternet : JobSearchScreenState

    data object ErrorServer : JobSearchScreenState

    data object Empty : JobSearchScreenState

    data object Default : JobSearchScreenState

    data object ShowPaginationLoading : JobSearchScreenState
}
