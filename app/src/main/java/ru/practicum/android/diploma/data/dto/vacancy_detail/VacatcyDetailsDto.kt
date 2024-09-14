package ru.practicum.android.diploma.data.dto.vacancy_detail

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.data.dto.Salary

data class VacancyDetailsDto(
    val name: String,
    val schedule: Schedule,
    @SerializedName("professional_roles")
    val professionalRoles: List<ProfessionalRole>,
    val salary: Salary?,
    val address: Address?,
    val experience: Experience,
    @SerializedName("key_skills")
    val keySkills: List<KeySkill>,
    @SerializedName("description")
    val description: String,
)

data class KeySkill(
    val name: String,
)

data class Experience(
    val name: String,

    )

data class Address(
    val city: String,
)

data class Schedule(
    val id: String,
    val name: String,
)

data class ProfessionalRole(
    val id: String,
    val name: String,
)

data class Salary(
    val currency: String,
    val from: Int
)





