package ru.practicum.android.diploma.domain.vacancydetails

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.VacancyDetailsModel

interface VacancyDetailsInteractor {
    suspend fun getVacancy(vacancyId: String): Flow<Resource<VacancyDetailsModel>>
}
