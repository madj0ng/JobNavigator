package ru.practicum.android.diploma.data.search

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.VacancyModel
import ru.practicum.android.diploma.domain.models.VacancySearchParams

interface SearchVacancyRepository {
    fun search(
        params: VacancySearchParams
    ): Flow<Resource<List<VacancyModel>>>
}
