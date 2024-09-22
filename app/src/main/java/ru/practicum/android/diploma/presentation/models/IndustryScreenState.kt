package ru.practicum.android.diploma.presentation.models

import ru.practicum.android.diploma.domain.models.IndustryModel

sealed interface IndustryScreenState {
    data class Content (val data: List<IndustryModel>): IndustryScreenState

    data object ErrorInternet: IndustryScreenState

    data object ErrorContent: IndustryScreenState
}
