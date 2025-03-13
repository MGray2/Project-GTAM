package com.example.gtam.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

// Database table for storing user settings and info for sending messages
@Entity(tableName = "user_bot")
data class UserBot(
    @PrimaryKey val id: Long = 1,
    val email: String?,
    val username: String?, // API key
    val password: String?, // API secret key
    val phoneNumber: String?,
    val messageSubject: String,
    val messageHeader: String,
    val messageFooter: String
)