package com.example.gtam.database.viewmodel

import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.gtam.MyApp
import com.example.gtam.database.dao.ServiceDao
import com.example.gtam.database.entities.Service
import com.example.gtam.database.repository.ServiceRepository
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

    private suspend fun serviceById(serviceId: MutableState<Long?>): Service {
        val nonNullLong: Long = serviceId.value ?: 0L
        return repository.getServiceById(nonNullLong).firstOrNull() ?: Service(id = nonNullLong, serviceName = "", servicePrice = 0.0, serviceDate = "")
    }

    private suspend fun serviceById(serviceId: Long): Service {
        return repository.getServiceById(serviceId).firstOrNull() ?: Service(id = serviceId, serviceName = "", servicePrice = 0.0, serviceDate = "")
    }

    fun addService(serviceId: MutableState<Long?>) {
        viewModelScope.launch {
            val service = serviceById(serviceId)
            _selectedServices.value = _selectedServices.value?.plus(service) ?: listOf(service)
        }
    }

    fun addService(service: Service) {
        _selectedServices.value = _selectedServices.value?.plus(service) ?: listOf(service)
    }

    fun removeService(serviceId: Long) {
        viewModelScope.launch {
            val service = serviceById(serviceId)
            _selectedServices.value = _selectedServices.value?.filterNot { it == service }
        }
    }

    init {
        allServices.observeForever { services ->
            _serviceDropdownList.value = services.map { it.id to it.serviceName }.toMutableList()
        }
    }
}