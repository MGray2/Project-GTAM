package com.example.gtam.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserBotDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(userBot: UserBot)

    @Query("SELECT * FROM user_bot WHERE id = 1 LIMIT 1")
    fun getUserBot(): Flow<UserBot?>

    @Query("UPDATE user_bot SET gmail = :gmail, outlook = :outlook, phoneNumber = :phoneNumber, messageHeader = :messageHeader, messageFooter = :messageFooter WHERE id = 1")
    suspend fun updateUserBot(gmail: String, outlook: String, phoneNumber: String, messageHeader: String, messageFooter: String)

    @Delete
    suspend fun deleteUserBot(userBot: UserBot)
}

@Dao
interface ServiceDao {
    @Insert
    suspend fun insertService(service: Service)

    @Query("SELECT * FROM services")
    fun getAllServices(): Flow<List<Service>>

    @Query("SELECT * FROM services WHERE id = :target")
    fun getServiceById(target: Long): Flow<Service?>

    @Delete
    suspend fun deleteService(service: Service)
}

@Dao
interface UserBotServiceCrossRefDao {
    @Insert
    suspend fun insertUserBotServiceCrossRef(userBotServiceCrossRef: UserBotServiceCrossRef)

    @Query("SELECT * FROM user_bot_services")
    fun getAllUserBotServices(): Flow<List<UserBotServiceCrossRef>>
}

@Dao
interface ClientDao {
    @Insert
    suspend fun insertClient(client: Client)

    @Query("SELECT * FROM clients")
    fun getAllClients(): Flow<List<Client>>

    @Delete
    suspend fun deleteClient(client: Client)

    @Query("SELECT * FROM clients WHERE id = :target")
    fun getClientById(target: Long): Flow<Client?>
}