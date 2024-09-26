package ru.practicum.android.diploma.data.dto.vacancy.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.dto.vacancy.VacancyRepository
import ru.practicum.android.diploma.data.dto.vacancydetail.VacancyDetailsRequest
import ru.practicum.android.diploma.data.dto.vacancydetail.VacancyDetailsResponse
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.domain.favoritejobs.FavoriteJobsRepository
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.VacancyDetailsModel
import ru.practicum.android.diploma.domain.vacancydetails.MapperVacancyDetails

class VacancyRepositoryImpl(
    private val retrofitNetworkClient: RetrofitNetworkClient,
    private val favoriteRepository: FavoriteJobsRepository,
    private val mapper: MapperVacancyDetails,
) : VacancyRepository {
    override suspend fun getVacancy(vacancyId: String): Flow<Resource<VacancyDetailsModel>> = flow {
        val vacancyDetailsRequest = VacancyDetailsRequest(vacancyId)
        val response = retrofitNetworkClient.doRequest(vacancyDetailsRequest)
//        return response as VacancyDetailsResponse
        when (response.resultCode) {
            RetrofitNetworkClient.RESULT_CODE_SUCCESS -> {
                val isfavoriteVacancy = favoriteRepository.getJob(vacancyId) != null
                val vacancyDetailsModel = mapper.map((response as VacancyDetailsResponse).vacancyDetails!!)
                emit(Resource.Success(vacancyDetailsModel.copy(isFavorite = isfavoriteVacancy)))
            }

            RetrofitNetworkClient.ERROR_CODE_INTERNET -> emit(Resource.Error(response.resultCode, response.message))
            else -> emit(Resource.Error(response.resultCode, response.message))
        }
    }
}
