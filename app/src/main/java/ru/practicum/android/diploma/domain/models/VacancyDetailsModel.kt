package ru.practicum.android.diploma.domain.models

data class VacancyDetailsModel(
    val name: String,
    val schedule: String,
    val professionalRoles: List<String>,
    val salary: String?,
    val address: String?,
    val experience: String,
    val keySkills: List<String>,
    val description: String,
    val employerIcon: String?,
    val employerName: String,

)
