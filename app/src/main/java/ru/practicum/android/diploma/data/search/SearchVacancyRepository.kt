package ru.practicum.android.diploma.data.search

import ru.practicum.android.diploma.data.dto.Vacancy

interface SearchVacancyRepository {
    suspend fun search(
        vacancyName: String,
        area: String = "",
        salary: Int,
        onlyWithSalary: Boolean = false,
        professionalRole: String = "",
    ): List<Vacancy>?

}
