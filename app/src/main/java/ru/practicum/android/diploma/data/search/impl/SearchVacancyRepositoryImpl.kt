package ru.practicum.android.diploma.data.search.impl

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.practicum.android.diploma.data.dto.VacancySearchRequest
import ru.practicum.android.diploma.data.dto.VacancySearchResponse
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.data.search.SearchVacancyRepository
import java.io.IOException

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
            try {
                val response = retrofitNetworkClient.doRequest(vacancyGoted)
                response as VacancySearchResponse
            } catch (e: IOException) {
                // Обработка ошибок сети, например, отсутствие соединения
                // Логирование или уведомление пользователя
                Log.d("Exception", "IOException")
                null
            } catch (e: HttpException) {
                // Обработка ошибок HTTP, например, 404 или 500
                // Логирование или уведомление пользователя
                Log.d("Exception", "HttpException")

                null
            }
        }
    }
}

