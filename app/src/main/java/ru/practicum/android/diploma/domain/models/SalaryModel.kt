package ru.practicum.android.diploma.domain.models

data class SalaryModel(
    val currency: String?,
    val from: Int?,
    val to: Int?,
    val gross: Boolean?
)
