package com.example.gtam.database.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.gtam.MyApp
import com.example.gtam.database.dao.ServiceDao
import com.example.gtam.database.entities.Service
import com.example.gtam.database.repository.ServiceRepository
import kotlinx.coroutines.launch

class ServiceViewModel(private val repository: ServiceRepository) : ViewModel() {

    val allServices: LiveData<List<Service>> = repository.getAllServices().asLiveData()

    fun insertService(serviceName: String, servicePrice: Double) {
        viewModelScope.launch {
            val newService = Service(serviceName = serviceName, servicePrice = servicePrice, serviceDate = null)
            repository.insertService(newService)
        }

    }

    fun deleteService(serviceID: Long) {
        viewModelScope.launch {
            repository.getServiceById(serviceID).collect { targetService ->
                if (targetService != null) {
                    repository.deleteService(targetService)
                }
            }
        }
    }
}