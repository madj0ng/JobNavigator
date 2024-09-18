package ru.practicum.android.diploma.data.db.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.domain.favoritejobs.FavoriteJobsRepository
import ru.practicum.android.diploma.domain.favoritejobs.mapper.MapperVacansyToJob
import ru.practicum.android.diploma.presentation.models.VacancyInfo

class FavoriteJobsRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val converter: MapperVacansyToJob
) : FavoriteJobsRepository {

    override suspend fun insertJob(vacancy: VacancyInfo) {
        appDatabase.jobDao().insertJob(converter.map(vacancy))
    }

    override suspend fun deleteJob(vacancy: VacancyInfo) {
        appDatabase.jobDao().deleteJob(converter.map(vacancy))
    }

    override suspend fun getJob(id: String): VacancyInfo {
        val vacancy = appDatabase.jobDao().getJob(id)
        return converter.map(vacancy)
    }

    override suspend fun getJobs(): Flow<List<VacancyInfo>> = flow {
        val list = appDatabase.jobDao().getJobs()
        emit(converter.mapList(list))
    }
}
