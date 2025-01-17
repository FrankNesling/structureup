package com.webie.structureup

// OS
import android.app.Application

// Coroutines
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

// References
import com.webie.structureup.database.StructureUpDB



class StructureUp : Application() {

    companion object {
        // Database
        lateinit var db: StructureUpDB

        fun getDatabase() : StructureUpDB {
            return db
        }

        // Coroutine
        lateinit var coroutineScope: CoroutineScope
    }

    override fun onCreate() {
        super.onCreate()

        // initialize DB
        db = StructureUpDB.getDatabase(this)

        // initialize coroutines for database operations outside of UI lifecycles
        coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    }
}