package ru.practicum.android.diploma.domain.vacancydetails

import ru.practicum.android.diploma.data.dto.vacancydetail.VacancyDetailsDto
import ru.practicum.android.diploma.domain.models.VacancyDetailsModel
import ru.practicum.android.diploma.util.FormatConverter

class MapperVacancyDetails(private val formatConverter: FormatConverter) {
    fun map(vacancyDetailsDto: VacancyDetailsDto): VacancyDetailsModel {
        return VacancyDetailsModel(
            vacancyDetailsDto.name,
            vacancyDetailsDto.schedule.name,
            vacancyDetailsDto.professionalRoles.map { it.name },
            formatConverter.toSalaryString(vacancyDetailsDto.salary),
            vacancyDetailsDto.address?.city,
            vacancyDetailsDto.experience.name,
            vacancyDetailsDto.keySkills.map { it.name },
            vacancyDetailsDto.description,
            vacancyDetailsDto.employer.logoUrls?.logo90,
            vacancyDetailsDto.employer.name,
            vacancyDetailsDto.alternativeUrl,
            false,
        )
    }
}
