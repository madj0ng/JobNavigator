package ru.practicum.android.diploma.data.dto.vacancydetail

import ru.practicum.android.diploma.data.dto.NetworkResponse

data class VacancyDetailsResponse(
    val vacancyDetails: VacancyDetailsDto
) : NetworkResponse()
