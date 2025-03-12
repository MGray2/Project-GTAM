package com.example.gtam.database.repository

import com.example.gtam.database.dao.MemoryDao
import com.example.gtam.database.entities.Memory

class MemoryRepository(private val memoryDao: MemoryDao) {

    // Get by Client id
    fun getMemoryByClient(clientId: Long): Memory? = memoryDao.getMemoryByClientId(clientId)

    // Insert
    suspend fun insertMemory(memory: Memory) {
        memoryDao.insertMemory(memory)
    }

    // Update
    suspend fun updateMemory(memory: Memory) {
        memoryDao.updateMemory(memory)
    }
}