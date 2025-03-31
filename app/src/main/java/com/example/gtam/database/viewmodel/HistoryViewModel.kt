package com.example.gtam.database.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.gtam.database.entities.History
import com.example.gtam.database.repository.HistoryRepository
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: HistoryRepository) : ViewModel() {

    val allHistory: LiveData<List<History>> = repository.getAllHistory().asLiveData()

    fun insertHistory(clientId: Long, type: String, status: Boolean, errorMessage: String?, date: String, subject: String, body: String) {
        val newHistory = History(
            clientId = clientId,
            type = type,
            status = status,
            errorMessage = errorMessage,
            date = date,
            subject = subject,
            body = body)
        viewModelScope.launch {
            repository.insertHistory(newHistory)
        }
    }

    fun deleteHistory(historyId: Long) {
        viewModelScope.launch {
            repository.getHistoryById(historyId).collect { targetHistory ->
                if (targetHistory != null) {
                    repository.deleteHistory(targetHistory)
                }
            }
        }
    }
}