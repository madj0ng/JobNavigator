package ru.practicum.android.diploma.domain.models

data class VacancyDetailsModelOrError(
    val error: Int,
    val vacancyDetailsModel: VacancyDetailsModel?,
)
