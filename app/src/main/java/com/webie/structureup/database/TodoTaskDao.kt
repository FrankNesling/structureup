package com.webie.structureup.database

// Database
import androidx.room.*

// References
import com.webie.structureup.model.TodoTask


@Dao
interface TodoTaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: TodoTask)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<TodoTask>)

    @Delete
    suspend fun delete(task: TodoTask)

    @Query("SELECT * FROM todo_tasks")
    suspend fun getAllTodoTasks(): List<TodoTask>

    @Query("SELECT * FROM todo_tasks WHERE date = :date")
    suspend fun getTasksForDate(date: Long): List<TodoTask>

    @Update
    suspend fun update(task: TodoTask)

}