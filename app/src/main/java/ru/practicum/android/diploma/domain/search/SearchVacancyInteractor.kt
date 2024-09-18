package ru.practicum.android.diploma.domain.search

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.VacancyModel
import ru.practicum.android.diploma.domain.models.VacancySearchParams

interface SearchVacancyInteractor {
    suspend fun searchVacancy(
        params: VacancySearchParams
    ): Flow<Resource<List<VacancyModel>>>
}
