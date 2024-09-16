package ru.practicum.android.diploma.data.dto.vacancy.impl

import ru.practicum.android.diploma.data.dto.vacancy.VacancyRepository
import ru.practicum.android.diploma.data.dto.vacancydetail.VacancyDetailsRequest
import ru.practicum.android.diploma.data.dto.vacancydetail.VacancyDetailsResponse
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient

class VacancyRepositoryImpl(private val retrofitNetworkClient: RetrofitNetworkClient) : VacancyRepository {
    override suspend fun getVacancy(vacancyId: String): VacancyDetailsResponse? {
        val vacancyDetailsRequest = VacancyDetailsRequest(vacancyId)
        val response = retrofitNetworkClient.doRequest(vacancyDetailsRequest)
        return response as VacancyDetailsResponse
    }
}
