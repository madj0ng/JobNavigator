package ru.practicum.android.diploma.domain.vacancy_details.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.dto.vacancy.VacancyRepository
import ru.practicum.android.diploma.domain.models.VacancyDetailsModel
import ru.practicum.android.diploma.domain.models.VacancyDetailsModelOrError
import ru.practicum.android.diploma.domain.vacancy_details.VacancyDetailsInteractor

class VacancyDetailsInteractorImpl(val vacancyRepository: VacancyRepository) : VacancyDetailsInteractor {
    override suspend fun getVacancy(vacancyId: String): Flow<VacancyDetailsModelOrError> = flow {
        val vacancyResponse = vacancyRepository.getVacancy(vacancyId)
        if (vacancyResponse?.resultCode != 200) {
            if (vacancyResponse != null) {
                emit(VacancyDetailsModelOrError(error = vacancyResponse.resultCode, vacancyDetailsModel = null))
            }
        } else {
            val vacancyRepositoryDto = vacancyResponse.vacancyDetails
            emit(
                VacancyDetailsModelOrError(
                    vacancyDetailsModel = VacancyDetailsModel(
                        vacancyRepositoryDto.name,
                        vacancyRepositoryDto.schedule.name,
                        vacancyRepositoryDto.professionalRoles.map { profession ->
                            profession.name
                        },
                        "от ${vacancyRepositoryDto.salary?.from} до ${vacancyRepositoryDto.salary?.to}",
                        vacancyRepositoryDto.address?.city,
                        vacancyRepositoryDto.experience.name,
                        vacancyRepositoryDto.keySkills.map { skill ->
                            skill.name
                        },
                        vacancyRepositoryDto.description
                    ), error = 0
                )
            )
        }
    }
}

