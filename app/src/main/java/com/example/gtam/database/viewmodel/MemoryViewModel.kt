package com.example.gtam.database.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gtam.database.entities.Memory
import com.example.gtam.database.entities.Service
import com.example.gtam.database.repository.MemoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MemoryViewModel(private val repository: MemoryRepository) : ViewModel() {

    private val _memory = MutableLiveData<Memory?>()
    val memory: LiveData<Memory?> get() = _memory

    fun clearMemory() {
        _memory.value = null
    }

    fun getMemoryByClient(clientId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val memoryData = repository.getMemoryByClient(clientId)
            _memory.postValue(memoryData)
        }
    }

    fun saveMemory(clientId: Long, subject: String, header: String, footer: String, serviceList: List<Service>) {
        viewModelScope.launch(Dispatchers.IO) {
            val current = repository.getMemoryByClient(clientId)
            val newMemory = Memory(
                id = current?.id ?: 0,
                clientId = clientId,
                subject = subject,
                header = header,
                footer = footer,
                services = serviceList
            )
            repository.insertMemory(newMemory)
            _memory.postValue(newMemory)
        }
    }
}