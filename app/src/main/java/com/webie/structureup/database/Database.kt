package com.webie.structureup.database

// OS
import android.content.Context

// Database
import androidx.room.*

// References
import com.webie.structureup.model.TodoTask

@Database(entities = [TodoTask::class], version = 1)
abstract class StructureUpDB : RoomDatabase() {
    abstract fun todoTaskDao(): TodoTaskDao

    companion object {
        @Volatile
        private var INSTANCE: StructureUpDB? = null

        fun getDatabase(context: Context): StructureUpDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StructureUpDB::class.java,
                    "structureUp-db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}