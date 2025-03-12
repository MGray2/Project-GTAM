package com.example.gtam.database.converters

import androidx.room.TypeConverter
import com.example.gtam.database.entities.Service
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson

class ServiceListConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromServiceList(services: List<Service>?): String {
        return gson.toJson(services)
    }

    @TypeConverter
    fun toServiceList(servicesJson: String?): List<Service> {
        return if (servicesJson.isNullOrEmpty()) emptyList()
        else gson.fromJson(servicesJson, object : TypeToken<List<Service>>() {}.type)
    }
}