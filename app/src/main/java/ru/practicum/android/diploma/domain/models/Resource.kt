package ru.practicum.android.diploma.domain.models

sealed interface Resource<T> {
    data class Success<T>(val data: T, val found: Int? = null) : Resource<T>
    data class Error<T>(val message: String) : Resource<T>
}
