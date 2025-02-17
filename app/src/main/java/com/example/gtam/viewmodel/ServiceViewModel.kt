package com.example.gtam.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.gtam.MyApp
import com.example.gtam.database.Service
import kotlinx.coroutines.launch

class ServiceViewModel : ViewModel() {
    private val serviceDAO = MyApp.database.serviceDao()

    val allServices: LiveData<List<Service>> = serviceDAO.getAllServices().asLiveData()

    fun insertService(serviceName: String, servicePrice: Double) {
        viewModelScope.launch {
            val newService = Service(serviceName = serviceName, servicePrice = servicePrice, serviceDate = null)
            serviceDAO.insertService(newService)
        }

    }

    fun deleteService(serviceID: Long) {
        viewModelScope.launch {
            serviceDAO.getServiceById(serviceID).collect { targetService ->
                if (targetService != null) {
                    serviceDAO.deleteService(targetService)
                }
            }
        }
    }
}