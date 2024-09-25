package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "job_table")
data class JobEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val salary: String = "",
    val employer: String = "",
    val logoUrl: String = "",
    val city: String,
)
