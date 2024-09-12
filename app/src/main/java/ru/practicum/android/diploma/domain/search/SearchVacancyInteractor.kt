package ru.practicum.android.diploma.domain.search

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.dto.VacancySearchResponse

interface SearchVacancyInteractor {
    suspend fun searchVacancy(
        vacancyName: String,
        area: String,
        salary: Int,
        onlyWithSalary: Boolean,
        professionalRole: String
    ): Flow<VacancySearchResponse?>

}
