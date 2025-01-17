package com.webie.structureup.database

// OS
import android.content.Context

// Database
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// References
import com.webie.structureup.model.TodoTask
import com.webie.structureup.model.DailyTask

@Database(entities = [TodoTask::class, DailyTask::class], version = 2)
abstract class StructureUpDB : RoomDatabase() {
    abstract fun todoTaskDao(): TodoTaskDao
    abstract fun dailyTaskDao(): DailyTaskDao

    companion object {
        @Volatile
        private var INSTANCE: StructureUpDB? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add the 'age' column to the 'user_table'
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS `daily_tasks` (
                        `id` TEXT NOT NULL PRIMARY KEY,
                        `title` TEXT NOT NULL
                    )
                """)
            }
        }

        fun getDatabase(context: Context): StructureUpDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StructureUpDB::class.java,
                    "structureUp-db"
                )
                .addMigrations(MIGRATION_1_2)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}