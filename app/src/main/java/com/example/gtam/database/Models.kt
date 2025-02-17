package com.example.gtam.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "user_bot")
data class UserBot(
    @PrimaryKey val id: Long = 1,
    val gmail: String?,
    val outlook: String?,
    val phoneNumber: String?,
    val messageHeader: String,
    val messageFooter: String
)

@Entity(tableName = "services")
data class Service(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val serviceName: String,
    val servicePrice: Double,
    val serviceDate: String?
)

@Entity(
    tableName = "user_bot_services",
    foreignKeys = [
        ForeignKey(entity = UserBot::class, parentColumns = ["id"], childColumns = ["userBotId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Service::class, parentColumns = ["id"], childColumns = ["serviceId"], onDelete = ForeignKey.CASCADE)
    ],
    primaryKeys = ["userBotId", "serviceId"]
)
data class UserBotServiceCrossRef(
    val userBotId: Long,
    val serviceId: Long
)

@Entity(tableName = "clients")
data class Client(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val clientName: String?,
    val clientEmail: String?,
    val clientPhoneNumber: String?,
    val clientAddress: String?
)