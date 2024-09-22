package ru.practicum.android.diploma.presentation.models

import androidx.annotation.DrawableRes

data class QueryUiState(
    @DrawableRes val src: Int? = null,
    val query: String = "",
    val isClose: Boolean = false
)
