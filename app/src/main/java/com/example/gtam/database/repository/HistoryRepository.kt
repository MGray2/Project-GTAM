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

    // Delete All
    suspend fun deleteAllHistory() {
        historyDao.deleteAllHistory()
    }

    // Get by id
    fun getHistoryById(historyId: Long): Flow<History?> {
        return historyDao.getHistoryById(historyId)
    }

    // Get a limited amount from an offset
    fun getHistoryPaged(limit: Int, offset: Int): Flow<List<History>> {
        return historyDao.getHistoryPaged(limit, offset)
    }
}