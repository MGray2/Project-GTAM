package com.example.gtam.database.repository

import com.example.gtam.database.dao.ServiceDao
import com.example.gtam.database.entities.Service
import kotlinx.coroutines.flow.Flow

class ServiceRepository(private val serviceDao: ServiceDao) {

    // Get all
    fun getAllServices(): Flow<List<Service>> = serviceDao.getAllServices()

    // Insert
    suspend fun insertService(service: Service) {
        serviceDao.insertService(service)
    }

    // Delete
    suspend fun deleteService(service: Service) {
        serviceDao.deleteService(service)
    }

    // Get by id
    fun getServiceById(serviceId: Long): Flow<Service?> {
        return serviceDao.getServiceById(serviceId)
    }
}