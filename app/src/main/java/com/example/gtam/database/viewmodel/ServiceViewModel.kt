package com.example.gtam.database.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.gtam.MyApp
import com.example.gtam.database.dao.ServiceDao
import com.example.gtam.database.entities.Memory
import com.example.gtam.database.entities.Service
import com.example.gtam.database.repository.ServiceRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class ServiceViewModel(private val repository: ServiceRepository) : ViewModel() {

    val allServices: LiveData<List<Service>> = repository.getAllServices().asLiveData()

    // List value used to hold all services that were selected
    private val _selectedServices = MutableLiveData<List<Service>>(emptyList())
    val selectedServices: LiveData<List<Service>> = _selectedServices

    // Value used to populate dropdowns with service names and their id
    private val _serviceDropdownList = MutableLiveData<List<Pair<Long, String>>>()
    val serviceDropdownList: LiveData<List<Pair<Long, String>>> = _serviceDropdownList

    // Used in activity 3, Inserts into Service DB table
    fun insertService(serviceName: String, servicePrice: Double) {
        viewModelScope.launch {
            val newService = Service(serviceName = serviceName, servicePrice = servicePrice, serviceDate = null)
            repository.insertService(newService)
        }

    }

    // Used in activity 3, Deletes instance of Service from DB
    fun deleteService(serviceID: Long) {
        viewModelScope.launch {
            repository.getServiceById(serviceID).collect { targetService ->
                if (targetService != null) {
                    repository.deleteService(targetService)
                }
            }
        }
    }

    // Helper function for changing MutableState<Long?> -> Service
    private suspend fun serviceById(serviceId: MutableState<Long?>): Service {
        val nonNullLong: Long = serviceId.value ?: 0L
        return repository.getServiceById(nonNullLong).firstOrNull() ?: Service(id = nonNullLong, serviceName = "", servicePrice = 0.0, serviceDate = "")
    }

    // Helper function for changing Long -> Service
    private suspend fun serviceById(serviceId: Long): Service {
        return repository.getServiceById(serviceId).firstOrNull() ?: Service(id = serviceId, serviceName = "", servicePrice = 0.0, serviceDate = "")
    }

    // Overload appends service to _selectedServices using service id
    fun addService(serviceId: MutableState<Long?>) {
        viewModelScope.launch {
            val service = serviceById(serviceId)
            _selectedServices.value = _selectedServices.value?.plus(service) ?: listOf(service)
        }
    }

    // Appends Service to _selectedServices
    fun addService(service: Service) {
        _selectedServices.value = _selectedServices.value?.plus(service) ?: listOf(service)
    }

    // Removes Service from _selectedServices by id
    fun removeService(serviceId: Long) {
        viewModelScope.launch {
            _selectedServices.value = _selectedServices.value?.filterNot { it.id == serviceId }
        }
    }

    fun clearServiceSelected() {
        _selectedServices.postValue(emptyList())
    }

    // Clears _selectedServices and adds Services from memory
    fun getServiceFromMemory(memory: Memory) {
        viewModelScope.launch(Dispatchers.IO) {
            _selectedServices.postValue(emptyList())
            _selectedServices.postValue(memory.services)
        }
    }

    init {
        allServices.observeForever { services ->
            _serviceDropdownList.value = services.map { it.id to it.serviceName }.toMutableList()
        }
    }
}