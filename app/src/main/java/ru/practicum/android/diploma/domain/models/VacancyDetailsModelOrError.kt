package ru.practicum.android.diploma.domain.models

data class VacancyDetailsModelOrError(
    var error: Int,
    var vacancyDetailsModel: VacancyDetailsModel?,
)
