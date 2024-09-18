package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName

data class VacancyDto(
    val id: String,
    val name: String,
    @SerializedName("employer") val employer: EmployerDto?,
    @SerializedName("salary") val salary: SalaryDto?
)
