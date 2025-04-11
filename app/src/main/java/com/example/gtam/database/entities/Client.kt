package com.example.gtam.database.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

// Database table for holding Client information
@Parcelize
@Entity(tableName = "clients")
data class Client(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val clientName: String?,
    val clientEmail: String?,
    val clientPhoneNumber: String?,
    val clientAddress: String?
) : Parcelable