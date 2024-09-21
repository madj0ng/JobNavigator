package ru.practicum.android.diploma.domain.models

sealed interface Resource<T> {
    data class Success<T>(
        val data: T,
        val found: Int? = null,
        val page: Int? = null,
        val pages: Int? = null
    ) : Resource<T>

    data class Error<T>(var resultCode: Int = 0, val message: String = "error") : Resource<T>
}
