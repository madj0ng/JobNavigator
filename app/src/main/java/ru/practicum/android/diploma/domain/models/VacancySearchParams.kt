package ru.practicum.android.diploma.domain.models

data class VacancySearchParams(
    val vacancyName: String = "",
    val area: String? = null,
    val salary: Int? = null,
    val onlyWithSalary: Boolean = true,
    val professionalRole: String? = null
)
