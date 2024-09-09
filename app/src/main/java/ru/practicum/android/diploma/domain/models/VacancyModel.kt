package ru.practicum.android.diploma.domain.models

data class VacancyModel(
    val id: String,
    val name: String,
    val salary: SalaryModel, // Размер заработной платы
    val employer: EmployerModel, // Информация о работодателе
)
