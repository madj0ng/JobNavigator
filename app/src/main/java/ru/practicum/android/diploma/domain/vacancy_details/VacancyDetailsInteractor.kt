package ru.practicum.android.diploma.domain.vacancy_details

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.VacancyDetailsModelOrError

interface VacancyDetailsInteractor {
    suspend fun getVacancy(vacancyId: String): Flow<VacancyDetailsModelOrError>
}
