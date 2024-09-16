package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.dto.VacancySearchResponse
import ru.practicum.android.diploma.data.dto.vacancydetail.VacancyDetailsDto

interface HHApiService {
    @GET("vacancies")
    @Headers("Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}")
    suspend fun getVacancies(@QueryMap options: Map<String, String>): VacancySearchResponse

    @GET("vacancies/{vacancy_id}")
    @Headers("Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}")
    suspend fun getVacancy(
        @Path("vacancy_id") vacancyId: String
    ): VacancyDetailsDto
}
