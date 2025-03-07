package com.example.gtam.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.gtam.database.entities.UserBot
import kotlinx.coroutines.flow.Flow

@Dao
interface UserBotDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserBot(userBot: UserBot)

    @Query("SELECT * FROM user_bot WHERE id = 1 LIMIT 1")
    fun getUserBot(): Flow<UserBot?>

    @Update
    suspend fun updateUserBot(userBot: UserBot)

    @Delete
    suspend fun deleteUserBot(userBot: UserBot)
}