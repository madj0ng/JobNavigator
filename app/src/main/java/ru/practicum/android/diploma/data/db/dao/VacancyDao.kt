package ru.practicum.android.diploma.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.practicum.android.diploma.data.db.entity.VacancyEntity

@Dao
interface VacancyDao {
    @Insert(entity = VacancyEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vacancyEntity: VacancyEntity)

    @Query("SELECT * FROM vacancy_table WHERE id = :id")
    suspend fun get(id: String): VacancyEntity?

    @Delete(entity = VacancyEntity::class)
    suspend fun delete(vacancyEntity: VacancyEntity)
}
