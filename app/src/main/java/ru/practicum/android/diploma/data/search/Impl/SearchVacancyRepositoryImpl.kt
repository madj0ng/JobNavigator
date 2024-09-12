package ru.practicum.android.diploma.data.search.Impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.data.dto.Vacancy
import ru.practicum.android.diploma.data.network.HHApiService
import ru.practicum.android.diploma.data.search.SearchVacancyRepository

class SearchVacancyRepositoryImpl(val hhApiService: HHApiService) : SearchVacancyRepository {
    override suspend fun search(
        vacancyName: String,
        area: String,
        salary: Int,
        onlyWithSalary: Boolean,
        professionalRole: String
    ): List<Vacancy>? {

        if (vacancyName.isEmpty()) {
            return null
        }
        return withContext(Dispatchers.IO) {
            try {
                val response = hhApiService.search(vacancyName, area, salary, onlyWithSalary, professionalRole)
                response.items
            } catch (e: Throwable) {
                null
            }
        }
    }
}

