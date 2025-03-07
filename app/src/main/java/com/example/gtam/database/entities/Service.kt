package com.example.gtam.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

// Database table for holding Service information
@Entity(tableName = "services")
data class Service(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val serviceName: String,
    val servicePrice: Double,
    var serviceDate: String?
)