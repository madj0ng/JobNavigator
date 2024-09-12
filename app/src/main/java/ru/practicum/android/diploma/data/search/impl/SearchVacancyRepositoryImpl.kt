package ru.practicum.android.diploma.data.search.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.practicum.android.diploma.data.dto.Vacancy
import ru.practicum.android.diploma.data.network.HHApiService
import ru.practicum.android.diploma.data.search.SearchVacancyRepository
import java.io.IOException

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
            } catch (e: IOException) {
                // Обработка ошибок сети, например, отсутствие соединения
                // Логирование или уведомление пользователя
                null
            } catch (e: HttpException) {
                // Обработка ошибок HTTP, например, 404 или 500
                // Логирование или уведомление пользователя
                null
            } catch (e: Exception) {
                // Обработка всех остальных исключений
                // Логирование или уведомление пользователя
                null
            }
        }
    }
}

