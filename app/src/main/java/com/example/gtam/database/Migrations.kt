package com.example.gtam.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migrations {
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // added clients to db
            database.execSQL(
                "CREATE TABLE IF NOT EXISTS `clients` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `clientName` TEXT NOT NULL, `clientEmail` TEXT, `clientPhoneNumber` TEXT)"
            )
        }
    }

    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `user_bot` (`id` INTEGER PRIMARY KEY NOT NULL, `gmail` TEXT, `outlook` TEXT, `phoneNumber` TEXT, `messageHeader` TEXT, `messageFooter` TEXT)")
        }
    }
}
