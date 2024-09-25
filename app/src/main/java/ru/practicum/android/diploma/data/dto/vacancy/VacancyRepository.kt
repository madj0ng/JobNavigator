package ru.practicum.android.diploma.data.dto.vacancy

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.VacancyDetailsModel

interface VacancyRepository {
    suspend fun getVacancy(
        vacancyId: String,
    ): Flow<Resource<VacancyDetailsModel>>
}
