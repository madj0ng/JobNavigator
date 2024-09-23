package ru.practicum.android.diploma.domain.vacancydetails.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.dto.vacancy.VacancyRepository
import ru.practicum.android.diploma.domain.favoritejobs.FavoriteJobsRepository
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.VacancyDetailsModel
import ru.practicum.android.diploma.domain.vacancydetails.MapperVacancyDetails
import ru.practicum.android.diploma.domain.vacancydetails.VacancyDetailsInteractor

class VacancyDetailsInteractorImpl(
    private val vacancyRepository: VacancyRepository,
) : VacancyDetailsInteractor {
    override suspend fun getVacancy(vacancyId: String): Flow<Resource<VacancyDetailsModel>> {
        return vacancyRepository.getVacancy(vacancyId)

//        if (vacancyResponse?.resultCode != RetrofitNetworkClient.RESULT_CODE_SUCCESS) {
//            if (vacancyResponse != null) {
//                emit(VacancyDetailsModelOrError(error = vacancyResponse.resultCode, vacancyDetailsModel = null))
//            }
//        } else {
//            val isfavoriteVacancy = favoriteRepository.getJob(vacancyId) != null
//            val vacancyDetailsModel = mapper.map(vacancyResponse.vacancyDetails)
//            emit(
//                VacancyDetailsModelOrError(
//                    vacancyDetailsModel = vacancyDetailsModel.copy(isFavorite = isfavoriteVacancy),
//                    error = RetrofitNetworkClient.RESULT_CODE_SUCCESS
//                )
//            )
//        }
    }
}
