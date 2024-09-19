package ru.practicum.android.diploma.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.practicum.android.diploma.data.db.entity.JobEntity

@Dao
interface JobDao {
    @Insert(entity = JobEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJob(jobEntity: JobEntity)

    @Delete(entity = JobEntity::class)
    suspend fun deleteJob(jobEntity: JobEntity)

    @Query("SELECT * FROM job_table WHERE id = :vacancyId")
    suspend fun getJob(vacancyId: String): JobEntity?

    @Query("SELECT * FROM job_table")
    suspend fun getJobs(): List<JobEntity>
}
