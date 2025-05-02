package com.example.gtam.database.repository

import com.example.gtam.database.dao.MessageDao
import com.example.gtam.database.entities.Message

class MessageRepository(private val messageDao: MessageDao) {

    suspend fun insert(message: Message): Long = messageDao.insert(message)

    suspend fun insertAndGetId(pendingMessage: Message): Long {
        return messageDao.insertAndGetId(pendingMessage)
    }

    suspend fun getById(id: Long): Message? = messageDao.getById(id)

    suspend fun delete(message: Message) = messageDao.delete(message)
}