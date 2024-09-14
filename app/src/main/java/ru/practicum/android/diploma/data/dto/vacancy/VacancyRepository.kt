package ru.practicum.android.diploma.data.dto.vacancy

import ru.practicum.android.diploma.data.dto.vacancy_detail.VacancyDetailsResponse

interface VacancyRepository {
    suspend fun getVacancy(
        vacancyId: String,
    ): VacancyDetailsResponse?
}
