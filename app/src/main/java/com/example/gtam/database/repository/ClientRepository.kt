package com.example.gtam.database.repository

import com.example.gtam.database.dao.ClientDao
import com.example.gtam.database.entities.Client
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class ClientRepository(private val clientDao: ClientDao) {

    // Get all
    fun getAllClients(): Flow<List<Client>> = clientDao.getAllClients()

    // Insert
    suspend fun insertClient(client: Client) {
        clientDao.insertClient(client)
    }

    // Delete
    suspend fun deleteClient(client: Client) {
        clientDao.deleteClient(client)
    }

    // Get by id
    fun getClientById(clientId: Long): Flow<Client?> {
        return clientDao.getClientById(clientId)
    }
}