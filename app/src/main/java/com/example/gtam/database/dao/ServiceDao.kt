package com.example.gtam.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.gtam.database.entities.Service
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiceDao {
    @Insert
    suspend fun insertService(service: Service)

    @Delete
    suspend fun deleteService(service: Service)

    @Query("SELECT * FROM services")
    fun getAllServices(): Flow<List<Service>>

    @Query("SELECT * FROM services WHERE id = :target")
    fun getServiceById(target: Long): Flow<Service?>

}