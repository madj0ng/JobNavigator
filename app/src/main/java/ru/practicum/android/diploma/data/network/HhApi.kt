package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.Headers
import ru.practicum.android.diploma.data.dto.VacancySearchResponse

interface HhApi {
    @Headers(
        "Authorization: Bearer accessToken",
        "HH-User-Agent: Job Navigator (klimushin1996@mail.ru)"
    )
    @GET("/vacancies")
    suspend fun search(
        @retrofit2.http.Query("search_field") request: String,
        accessToken: String
    ): VacancySearchResponse
}

