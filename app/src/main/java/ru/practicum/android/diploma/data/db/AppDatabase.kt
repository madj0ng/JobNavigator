package ru.practicum.android.diploma.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.practicum.android.diploma.data.db.dao.JobDao
import ru.practicum.android.diploma.data.db.dao.VacancyDao
import ru.practicum.android.diploma.data.db.entity.JobEntity
import ru.practicum.android.diploma.data.db.entity.VacancyEntity

@Database(
    version = 1,
    entities = [JobEntity::class, VacancyEntity::class]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun jobDao(): JobDao
    abstract fun vacancyDao(): VacancyDao
}
