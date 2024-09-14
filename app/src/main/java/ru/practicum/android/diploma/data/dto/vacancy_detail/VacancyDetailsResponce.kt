package ru.practicum.android.diploma.data.dto.vacancy_detail

import ru.practicum.android.diploma.data.dto.NetworkResponse

data class VacancyDetailsResponse(
    val vacancyDetails: VacancyDetailsDto
): NetworkResponse()
