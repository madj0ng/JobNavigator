package ru.practicum.android.diploma.domain.models

data class VacancyModel(
    val id: String,
    val name: String,
    val salary: SalaryModel? = null, // Размер заработной платы
    val employer: EmployerModel? = null // Информация о работодателе
)
