package ru.practicum.android.diploma.presentation.models

data class VacancyInfo(
    val id: String,
    val vacancyName: String,
    val departamentName: String,
    val salary: String,
    val logoUrl: String? = null,
)
