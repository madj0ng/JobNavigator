package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "job_table")
data class JobEntity(
    @PrimaryKey
    val vacancyId: String,
)
