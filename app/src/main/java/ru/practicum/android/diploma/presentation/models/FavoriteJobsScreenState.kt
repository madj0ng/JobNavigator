package ru.practicum.android.diploma.presentation.models

sealed interface FavoriteJobsScreenState {
    data object Error : FavoriteJobsScreenState

    data object Empty : FavoriteJobsScreenState

    data class Content(val data: List<VacancyInfo>) : FavoriteJobsScreenState
}
