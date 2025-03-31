package com.example.gtam.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


// Database table for holding preference data for auto-filling forms
@Entity(tableName = "memory")
data class Memory(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "client_id") val clientId: Long,
    val subject: String,
    val header: String,
    val footer: String,
    val services: List<Service>
)