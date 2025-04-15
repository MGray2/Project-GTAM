package com.example.gtam.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.gtam.database.entities.History
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Insert
    suspend fun insertHistory(history: History)

    @Delete
    suspend fun deleteHistory(history: History)

    @Query("DELETE FROM history")
    suspend fun deleteAllHistory()

    @Query("SELECT * FROM history ORDER BY timestamp DESC")
    fun getAllHistory(): Flow<List<History>>

    @Query("SELECT * FROM history WHERE id = :historyId")
    fun getHistoryById(historyId: Long): Flow<History?>

    @Query("SELECT * FROM history ORDER BY timestamp DESC LIMIT :limit OFFSET :offset")
    fun getHistoryPaged(limit: Int, offset: Int): Flow<List<History>>
}