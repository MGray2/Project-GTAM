package com.example.gtam.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migrations {
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Create 'clients' table on version 2
            database.execSQL(
                "CREATE TABLE IF NOT EXISTS `clients` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `clientName` TEXT NOT NULL, `clientEmail` TEXT, `clientPhoneNumber` TEXT)"
            )
        }
    }
}
