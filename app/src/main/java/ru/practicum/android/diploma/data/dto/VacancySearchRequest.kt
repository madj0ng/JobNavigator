package ru.practicum.android.diploma.data.dto

data class VacancySearchRequest(
    val vacancyName: String,
    val area: String,
    val salary: Int,
    val onlyWithSalary: Boolean,
    val professionalRole: String
)
