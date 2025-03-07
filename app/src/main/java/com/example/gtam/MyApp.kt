package com.example.gtam

import android.app.Application
import androidx.room.Room
import com.example.gtam.database.AppDatabase
import com.example.gtam.database.Migrations
import com.example.gtam.database.repository.UserBotRepository


class MyApp : Application() {
    companion object {
        lateinit var database: AppDatabase
        lateinit var userBotRepository: UserBotRepository
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).addMigrations().build()

        userBotRepository = UserBotRepository(database.userBotDao())
    }
}