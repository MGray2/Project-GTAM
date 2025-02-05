package com.example.gtam.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserBotDao {
    @Insert
    suspend fun insertUserBot(userBot: UserBot)

    @Query("SELECT * FROM user_bots")
    fun getAllUserBots(): Flow<List<UserBot>>

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
}