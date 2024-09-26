package ru.practicum.android.diploma.domain.favoritejobs.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.favoritejobs.FavoriteJobsInteractor
import ru.practicum.android.diploma.domain.favoritejobs.FavoriteJobsRepository
import ru.practicum.android.diploma.domain.models.VacancyDetailsModel
import ru.practicum.android.diploma.presentation.models.VacancyInfo

class FavoriteJobsInteractorImpl(
    private val repository: FavoriteJobsRepository
) : FavoriteJobsInteractor {

    override suspend fun insertJob(vacancy: VacancyInfo) {
        repository.insertJob(vacancy)
    }

    override suspend fun deleteJob(vacancy: VacancyInfo) {
        repository.deleteJob(vacancy)
    }

    override suspend fun getJob(id: String): VacancyInfo? {
        return repository.getJob(id)
    }

    override suspend fun getJobs(): Flow<List<VacancyInfo>> {
        return repository.getJobs()
    }

    override suspend fun insertVacancy(id: String, vacancy: VacancyDetailsModel) {
        repository.insertVacancy(id, vacancy)
    }

    override suspend fun deleteVacancy(id: String, vacancy: VacancyDetailsModel) {
        repository.deleteVacancy(id, vacancy)
    }

    override suspend fun getVacancy(id: String): Flow<VacancyDetailsModel?> {
        return repository.getVacancy(id)
    }
}
