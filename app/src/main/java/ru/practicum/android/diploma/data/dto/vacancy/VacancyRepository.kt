package ru.practicum.android.diploma.data.dto.vacancy

import ru.practicum.android.diploma.data.dto.vacancydetail.VacancyDetailsResponse

interface VacancyRepository {
    suspend fun getVacancy(
        vacancyId: String,
    ): VacancyDetailsResponse?
}
