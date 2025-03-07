package com.example.gtam.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.gtam.database.converters.ServiceIdConverter
import com.example.gtam.database.dao.*
import com.example.gtam.database.entities.*

@Database(entities = [UserBot::class, Service::class, Client::class, Memory::class], version = 1, exportSchema = false)
@TypeConverters(ServiceIdConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userBotDao() : UserBotDao
    abstract fun serviceDao() : ServiceDao
    abstract fun clientDao() : ClientDao
    abstract fun memoryDao() : MemoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

