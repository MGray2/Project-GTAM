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

    @Query("SELECT * FROM user_bot LIMIT 1")
    suspend fun getUserBotOnce(): UserBot?

    @Update
    suspend fun updateUserBot(userBot: UserBot)

    @Query("UPDATE user_bot SET email = :email, mjApiKey = :mjApiKey, mjSecretKey = :mjSecretKey, nvApiKey = :nvApiKey WHERE id = 1")
    suspend fun updateBotInfo(
        email: String,
        mjApiKey: String,
        mjSecretKey: String,
        nvApiKey: String
    )

    @Query("UPDATE user_bot SET emailSubject = :emailSubject, emailHeader = :emailHeader, emailFooter = :emailFooter, textHeader = :textHeader, textFooter = :textFooter")
    suspend fun updateBotDefaults(
        emailSubject: String,
        emailHeader: String,
        emailFooter: String,
        textHeader: String,
        textFooter: String
    )

    @Delete
    suspend fun deleteUserBot(userBot: UserBot)
}