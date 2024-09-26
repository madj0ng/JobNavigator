package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vacancy_table")
data class VacancyEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val schedule: String,
    val professionalRoles: String,
    val salary: String,
    val address: String,
    val experience: String,
    val keySkills: String,
    val description: String,
    val employerIcon: String?,
    val employerName: String,
    val alternativeUrl: String,
    val isFavorite: Boolean,
)
