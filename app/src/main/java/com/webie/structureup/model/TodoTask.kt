package com.webie.structureup.model

// Database
import androidx.room.Entity
import androidx.room.PrimaryKey

// Data Types


@Entity(tableName = "todo_tasks")
data class TodoTask(
    @PrimaryKey val id: String,
    val title: String = "",
    val description: String = "",
    val date: Long,
    val isCompleted: Boolean = false
)