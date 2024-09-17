package ru.practicum.android.diploma.domain.favoritejobs.mapper

import ru.practicum.android.diploma.data.db.entity.JobEntity
import ru.practicum.android.diploma.presentation.models.VacancyInfo

class MapperVacansyToJob {

    fun map(vacancyInfo: VacancyInfo): JobEntity {
        return JobEntity(
            vacancyInfo.id,
            vacancyInfo.vacancyName,
            vacancyInfo.salary,
            vacancyInfo.departamentName,
            vacancyInfo.logoUrl ?: ""
        )
    }

    fun map(jobEntity: JobEntity): VacancyInfo {
        return VacancyInfo(
            jobEntity.id,
            jobEntity.name,
            jobEntity.employer,
            jobEntity.salary,
            jobEntity.logoUrl
        )
    }

    fun mapList(list: List<JobEntity>): List<VacancyInfo> {
        return list.map {
            map(it)
        }
    }
}
