package ru.practicum.android.diploma.domain.vacancydetails.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.dto.vacancy.VacancyRepository
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.VacancyDetailsModel
import ru.practicum.android.diploma.domain.vacancydetails.VacancyDetailsInteractor

class VacancyDetailsInteractorImpl(
    private val vacancyRepository: VacancyRepository,
) : VacancyDetailsInteractor {
    override suspend fun getVacancy(vacancyId: String): Flow<Resource<VacancyDetailsModel>> {
        return vacancyRepository.getVacancy(vacancyId)
    }
}
