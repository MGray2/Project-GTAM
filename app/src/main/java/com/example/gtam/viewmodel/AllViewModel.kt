package com.example.gtam.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.gtam.MyApp
import com.example.gtam.database.Client

class AllViewModel : ViewModel() {
    private val clientDAO = MyApp.database.clientDao()

    val allClients: LiveData<List<Client>> = clientDAO.getAllClients().asLiveData()
}