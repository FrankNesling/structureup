package com.webie.structureup.database

// Database
import androidx.room.*

// References
import com.webie.structureup.model.DailyTask

@Dao
interface DailyTaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: DailyTask)

    @Delete
    suspend fun delete(task: DailyTask)

    @Query("SELECT * FROM daily_tasks")
    suspend fun getAllDailyTasks(): List<DailyTask>
}