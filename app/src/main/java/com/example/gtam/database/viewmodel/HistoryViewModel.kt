package com.example.gtam.database.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.gtam.database.entities.History
import com.example.gtam.database.repository.HistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: HistoryRepository) : ViewModel() {

    private val _offset = mutableIntStateOf(0)
    private val _pagedHistory = MutableStateFlow<List<History>>(emptyList())
    private val _isEndReached = mutableStateOf(false)
    private val _isStartReached = mutableStateOf(true)

    val isEndReached: State<Boolean> = _isEndReached
    val isStartReached: State<Boolean> = _isStartReached
    val pagedHistory: StateFlow<List<History>> = _pagedHistory
    val allHistory: LiveData<List<History>> = repository.getAllHistory().asLiveData()

    private val limit = 15

    fun insertHistory(
        clientName: String?,
        clientAddress: String?,
        clientEmail: String?,
        clientPhone: String? ,
        type: String,
        status: Boolean,
        errorMessage: String?,
        date: String,
        subject: String,
        body: String
    ) {
        val newHistory = History(
            clientName = clientName,
            clientAddress = clientAddress,
            clientEmail = clientEmail,
            clientPhone = clientPhone,
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

    fun deleteAllHistory() {
        viewModelScope.launch {
            repository.deleteAllHistory()
        }
    }

    fun loadPage(forward: Boolean) {
        viewModelScope.launch {
            val newOffset = if (forward) _offset.intValue + limit else (_offset.intValue - limit).coerceAtLeast(0)
            repository.getHistoryPaged(limit, newOffset).collect { newData ->
                if (newData.size < limit) _isEndReached.value = true
                _pagedHistory.value = newData
                _offset.intValue = newOffset
                _isStartReached.value = newOffset == 0
                _isEndReached.value = newData.size < limit
            }
        }
    }

    init {
        loadPage(forward = false)
    }
}