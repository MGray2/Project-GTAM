package com.example.gtam.database.repository

import com.example.gtam.database.dao.UserBotDao
import com.example.gtam.database.entities.UserBot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class UserBotRepository(private val userBotDao: UserBotDao) {
    // Insert
    suspend fun insertUserBot(userBot: UserBot) {
        withContext(Dispatchers.IO) {
            userBotDao.insertUserBot(userBot)
        }
    }

    // Update
    suspend fun updateUserBot(userBot: UserBot) {
        withContext(Dispatchers.IO) {
            userBotDao.updateUserBot(userBot)
        }
    }

    // Delete
    suspend fun deleteUserBot(userBot: UserBot) {
        withContext(Dispatchers.IO) {
            userBotDao.deleteUserBot(userBot)
        }
    }

    // Get one instance
    suspend fun getUserBot(): Flow<UserBot?> {
        return withContext(Dispatchers.IO) {
            userBotDao.getUserBot()
        }
    }
}