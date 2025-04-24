package com.example.gtam.database.converters

import androidx.room.TypeConverter
import com.example.gtam.database.entities.Service
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson

class ServiceListConverter {

    companion object {
        private val serviceListType = object : TypeToken<List<Service>>() {}.type
    }

    @TypeConverter
    fun fromServiceList(services: List<Service>?): String {
        return Gson().toJson(services)
    }

    @TypeConverter
    fun toServiceList(servicesJson: String?): List<Service> {
        return if (servicesJson.isNullOrEmpty()) emptyList()
        else Gson().fromJson(servicesJson, serviceListType)
    }
}