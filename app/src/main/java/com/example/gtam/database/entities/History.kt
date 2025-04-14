package com.example.gtam.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// Database table for holding messaging history
@Parcelize
@Entity(tableName = "history")
data class History(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val clientName: String?,
    val clientAddress: String?,
    val clientEmail: String?,
    val clientPhone: String?,
    val type: String,
    val status: Boolean,
    val errorMessage: String?,
    val timestamp: Long = System.currentTimeMillis(),
    val date: String,
    val subject: String,
    val body: String
) : Parcelable
