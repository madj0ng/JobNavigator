package ru.practicum.android.diploma.domain.favoritejobs

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.VacancyDetailsModel
import ru.practicum.android.diploma.presentation.models.VacancyInfo

interface FavoriteJobsInteractor {

    suspend fun insertJob(vacancy: VacancyInfo)

    suspend fun deleteJob(vacancy: VacancyInfo)

    suspend fun getJob(id: String): VacancyInfo?

    suspend fun getJobs(): Flow<List<VacancyInfo>>

    suspend fun insertVacancy(id: String, vacancy: VacancyDetailsModel)

    suspend fun deleteVacancy(id: String, vacancy: VacancyDetailsModel)

    suspend fun getVacancy(id: String): Flow<VacancyDetailsModel?>
}
