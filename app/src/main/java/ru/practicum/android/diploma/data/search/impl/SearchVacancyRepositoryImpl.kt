package ru.practicum.android.diploma.data.search.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.data.dto.VacancySearchRequest
import ru.practicum.android.diploma.data.dto.VacancySearchResponse
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.data.search.SearchVacancyRepository

class SearchVacancyRepositoryImpl(private val retrofitNetworkClient: RetrofitNetworkClient) : SearchVacancyRepository {
    override suspend fun search(
        vacancyName: String,
        area: String,
        salary: Int,
        onlyWithSalary: Boolean,
        professionalRole: String
    ): VacancySearchResponse? {
        if (vacancyName.isEmpty()) {
            return null
        }
        val vacancyGoted = VacancySearchRequest(vacancyName, area, salary, onlyWithSalary, professionalRole)
        return withContext(Dispatchers.IO) {
            val response = retrofitNetworkClient.doRequest(vacancyGoted)
            response as VacancySearchResponse
        }
    }
}
