package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.dto.VacancySearchResponse

interface HHApiService {
    @GET("vacancies")
    @Headers("Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}")
    suspend fun getVacancies(@QueryMap options: Map<String, String>): VacancySearchResponse
}
