package com.example.gtam.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gtam.database.entities.Message

@Dao
interface MessageDao {
    @Insert
    suspend fun insert(message: Message): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAndGetId(message: Message): Long

    @Query("SELECT * FROM Message WHERE id = :id")
    suspend fun getById(id: Long): Message?

    @Delete
    suspend fun delete(message: Message)
}