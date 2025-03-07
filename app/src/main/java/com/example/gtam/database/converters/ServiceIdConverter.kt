package com.example.gtam.database.converters

import androidx.room.TypeConverter

class ServiceIdConverter {
    @TypeConverter
    fun fromList(serviceIds: List<Long>): String {
        return serviceIds.joinToString(",")
    }

    @TypeConverter
    fun toList(data: String): List<Long> {
        return if (data.isEmpty()) emptyList() else data.split(",").map { it.toLong() }
    }
}