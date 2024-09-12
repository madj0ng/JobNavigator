package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.dto.SearchResponse

interface HHApiService {
    @GET("vacancies")
    @Headers("Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}")
    suspend fun getVacancies(
        @Query("text") job: String = "",
        @Query("area") area: String? = null,
        @Query("salary") salary: Int? = null,
        @Query("only_with_salary") onlyWithSalary: Boolean = false,
        @Query("professional_role") professionalRole: String? = null,
    ): SearchResponse
}
