package ru.practicum.android.diploma.domain.search.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.dto.VacancySearchResponse
import ru.practicum.android.diploma.data.search.SearchVacancyRepository
import ru.practicum.android.diploma.domain.search.SearchVacancyInteractor

class SearchVacancyInteractorImpl(val searchVacancyRepository: SearchVacancyRepository) : SearchVacancyInteractor {
    override suspend fun searchVacancy(
        vacancyName: String,
        area: String,
        salary: Int,
        onlyWithSalary: Boolean,
        professionalRole: String
    ): Flow<VacancySearchResponse?> = flow {
        emit(
            searchVacancyRepository.search(
                vacancyName,
                area,
                salary,
                onlyWithSalary,
                professionalRole
            )
        )
    }
}
