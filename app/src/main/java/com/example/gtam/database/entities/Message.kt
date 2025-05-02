package com.example.gtam.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Message (
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val clientId: Long,
    val subject: String,
    val header: String,
    val footer: String,
    val servicesJson: String
)