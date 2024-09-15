package ru.practicum.android.diploma.domain.vacancy_details.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.dto.Salary
import ru.practicum.android.diploma.data.dto.vacancy.VacancyRepository
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.domain.models.SalaryModel
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
                        getSalary(vacancyRepositoryDto.salary),
                        vacancyRepositoryDto.address?.city,
                        vacancyRepositoryDto.experience.name,
                        vacancyRepositoryDto.keySkills.map { skill ->
                            skill.name
                        },
                        vacancyRepositoryDto.description,
                        vacancyRepositoryDto.employer.logoUrls?.logo90,
                        vacancyRepositoryDto.employer.name,
                    ), error = RetrofitNetworkClient.RESULT_CODE_SUCCESS
                )
            )
        }
    }

    private fun getSalary(salary: Salary?): String {
        if (salary == null) {
            return "Зарплата не указана"
        }

        val from = getSalaryFrom(salary.from)
        val to = getSalaryTo(salary.to)
        val currency = salary.currency?.let { " $it" } ?: ""

        return if (from.isEmpty() && to.isEmpty()) {
            "Зарплата не указана"
        } else {
            "$from$to$currency"
        }
    }

    private fun getSalaryFrom(from: Int?): String {
        return from?.let { "от $it" } ?: ""
    }

    private fun getSalaryTo(to: Int?): String {
        return to?.let { " до $it" } ?: ""
    }
}

