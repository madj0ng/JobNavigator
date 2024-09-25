package ru.practicum.android.diploma.domain.favoritejobs.mapper

import com.google.gson.Gson
import ru.practicum.android.diploma.data.db.entity.JobEntity
import ru.practicum.android.diploma.data.db.entity.VacancyEntity
import ru.practicum.android.diploma.domain.models.VacancyDetailsModel
import ru.practicum.android.diploma.presentation.models.VacancyInfo

class MapperVacansyToJob(private val gson: Gson) {

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

    fun map(id: String, vacancy: VacancyDetailsModel): VacancyEntity {
        val professionalRoles = gson.toJson(vacancy.professionalRoles)
        val keySkills = gson.toJson(vacancy.keySkills)
        return VacancyEntity(
            id,
            vacancy.name,
            vacancy.schedule,
            professionalRoles,
            vacancy.salary ?: "",
            vacancy.address ?: "",
            vacancy.experience,
            keySkills,
            vacancy.description,
            vacancy.employerIcon,
            vacancy.employerName,
            vacancy.alternativeUrl,
            true
        )
    }

    fun map(vacancy: VacancyEntity): VacancyDetailsModel {
        val professionalRoles = gson.fromJson(vacancy.professionalRoles, Array<String>::class.java)
        val keySkills = gson.fromJson(vacancy.keySkills, Array<String>::class.java)
        return VacancyDetailsModel(
            vacancy.name,
            vacancy.schedule,
            professionalRoles.toList(),
            vacancy.salary,
            vacancy.address,
            vacancy.experience,
            keySkills.toList(),
            vacancy.description,
            vacancy.employerIcon,
            vacancy.employerName,
            vacancy.alternativeUrl,
            true
        )
    }
}
