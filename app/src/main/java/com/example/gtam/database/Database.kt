package com.example.gtam.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserBot::class, Service::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userBotDao() : UserBotDao
    abstract fun serviceDao() : ServiceDao
}