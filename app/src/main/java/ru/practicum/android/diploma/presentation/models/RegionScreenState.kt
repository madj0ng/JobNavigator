package ru.practicum.android.diploma.presentation.models

import ru.practicum.android.diploma.domain.models.RegionModel

sealed interface RegionScreenState {
    data class Content(val data: List<RegionModel>) : RegionScreenState

    data object ErrorNoList : RegionScreenState

    data object ErrorNoRegion : RegionScreenState
}
