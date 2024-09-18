package ru.practicum.android.diploma.domain.search.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.search.SearchVacancyRepository
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.VacancyModel
import ru.practicum.android.diploma.domain.models.VacancySearchParams
import ru.practicum.android.diploma.domain.search.SearchVacancyInteractor

class SearchVacancyInteractorImpl(val searchVacancyRepository: SearchVacancyRepository) : SearchVacancyInteractor {
    override suspend fun searchVacancy(
        params: VacancySearchParams
    ): Flow<Resource<List<VacancyModel>>> {
        return searchVacancyRepository.search(params)
    }
}
