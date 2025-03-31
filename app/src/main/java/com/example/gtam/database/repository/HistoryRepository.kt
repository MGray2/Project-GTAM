package com.example.gtam.database.repository

import com.example.gtam.database.dao.HistoryDao
import com.example.gtam.database.entities.History
import kotlinx.coroutines.flow.Flow

class HistoryRepository(private val historyDao: HistoryDao) {

    // Get all
    fun getAllHistory(): Flow<List<History>> = historyDao.getAllHistory()

    // Insert
    suspend fun insertHistory(history: History) {
        historyDao.insertHistory(history)
    }

    // Delete
    suspend fun deleteHistory(history: History) {
        historyDao.deleteHistory(history)
    }

    // Get by id
    fun getHistoryById(historyId: Long): Flow<History?> {
        return historyDao.getHistoryById(historyId)
    }

    // Get by timestamp: last 30 days
    fun getEmailCountLast30Days(clientId: Long): Flow<Int> {
        val cutoffTime = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000) // 30 days in milliseconds
        return historyDao.getEmailCountLast30Days(clientId, cutoffTime)
    }
}