package com.webie.structureup.model

// Database
import androidx.room.Entity
import androidx.room.PrimaryKey

// Data Types


@Entity(tableName = "daily_tasks")
data class DailyTask(
    @PrimaryKey val id: String,
    val title: String,
)