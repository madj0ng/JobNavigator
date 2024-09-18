package ru.practicum.android.diploma.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.practicum.android.diploma.data.db.dao.JobDao
import ru.practicum.android.diploma.data.db.entity.JobEntity

@Database(
    version = 1,
    entities = [JobEntity::class]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun jobDao(): JobDao
}
