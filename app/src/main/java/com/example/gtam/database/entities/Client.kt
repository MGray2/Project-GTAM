package com.example.gtam.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

// Database table for holding Client information
@Entity(tableName = "clients")
data class Client(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val clientName: String?,
    val clientEmail: String?,
    val clientPhoneNumber: String?,
    val clientAddress: String?
)