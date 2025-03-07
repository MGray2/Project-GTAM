package com.example.gtam.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.gtam.database.entities.Memory

@Dao
interface MemoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemory(memory: Memory)

    @Query("SELECT * FROM memory WHERE client_id = :target LIMIT 1")
    fun getMemoryByClientId(target: Long): Memory?

    @Update
    fun updateMemory(memory: Memory)
}