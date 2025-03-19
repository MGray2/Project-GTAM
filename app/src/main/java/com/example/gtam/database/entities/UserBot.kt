package com.example.gtam.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

// Database table for storing user settings and info for sending messages
@Entity(tableName = "user_bot")
data class UserBot(
    @PrimaryKey val id: Long = 1,
    val email: String?,
    val mjApiKey: String?, // Mailjet API key
    val mjSecretKey: String?, // Mailjet API secret key
    val nvApiKey: String?, // NumVerify API key
    val messageSubject: String,
    val messageHeader: String,
    val messageFooter: String
)