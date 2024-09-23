package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.dto.VacancySearchResponse
import ru.practicum.android.diploma.data.dto.vacancydetail.VacancyDetailsDto
import ru.practicum.android.diploma.data.filter.AreaResponse
import ru.practicum.android.diploma.data.filter.IndustryResponse

interface HHApiService {
    @GET("vacancies")
    @Headers(
        "Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}",
        "HH-User-Agent: JobNavigator (klimushin1996@mail.ru)"
    )
    suspend fun getVacancies(@QueryMap options: Map<String, String>): VacancySearchResponse

    @GET("vacancies/{vacancy_id}")
    @Headers(
        "Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}",
        "HH-User-Agent: JobNavigator (klimushin1996@mail.ru)"
    )
    suspend fun getVacancy(
        @Path("vacancy_id") vacancyId: String
    ): VacancyDetailsDto

    @GET("industries")
    @Headers(
        "Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}",
        "HH-User-Agent: JobNavigator (klimushin1996@mail.ru)"
    )
    suspend fun getIndustries(): List<IndustryResponse>

    @GET("areas")
    @Headers(
        "Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}",
        "HH-User-Agent: JobNavigator (klimushin1996@mail.ru)"
    )
    suspend fun getAreas(): List<AreaResponse>
}
