package ru.practicum.android.diploma.domain.models

data class VacancySearchParams(
    val vacancyName: String = "",
    val area: String? = null,
    val salary: Int? = null,
    val onlyWithSalary: Boolean = false,
    val professionalRole: String? = null,
    val page: Int = 0,
    val perPage: Int = 20
)
