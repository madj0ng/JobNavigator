package ru.practicum.android.diploma.presentation.models

import androidx.annotation.DrawableRes
import ru.practicum.android.diploma.R

sealed interface QueryUiState {
    val src: Int?
    val query: String

    data class Search(
        @DrawableRes override val src: Int = R.drawable.ic_close_cross,
        override val query: String = ""
    ) : QueryUiState

    data class Clear(
        @DrawableRes override val src: Int = R.drawable.ic_search_lens,
        override val query: String = ""
    ) : QueryUiState
}
