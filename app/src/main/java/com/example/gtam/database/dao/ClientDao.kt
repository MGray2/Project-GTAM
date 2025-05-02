package com.example.gtam.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gtam.database.entities.Client
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClient(client: Client)

    @Delete
    suspend fun deleteClient(client: Client)

    @Query("SELECT * FROM clients")
    fun getAllClients(): Flow<List<Client>>

    @Query("SELECT * FROM clients WHERE id = :target")
    fun getClientById(target: Long): Flow<Client?>

    @Query("SELECT * FROM clients WHERE id = :target")
    suspend fun getClientByIdOnce(target: Long): Client?
}