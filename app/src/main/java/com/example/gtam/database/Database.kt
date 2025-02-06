package com.example.gtam.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserBot::class, Service::class, Client::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userBotDao() : UserBotDao
    abstract fun serviceDao() : ServiceDao
    abstract fun clientDao() : ClientDao
}