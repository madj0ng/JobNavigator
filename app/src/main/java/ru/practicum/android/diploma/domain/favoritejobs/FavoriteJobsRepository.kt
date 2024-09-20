package ru.practicum.android.diploma.domain.favoritejobs

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.presentation.models.VacancyInfo

interface FavoriteJobsRepository {

    suspend fun insertJob(vacancy: VacancyInfo)

    suspend fun deleteJob(vacancy: VacancyInfo)

    suspend fun getJob(id: String): VacancyInfo?

    suspend fun getJobs(): Flow<List<VacancyInfo>>
}
