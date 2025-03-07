package com.example.gtam.database.viewmodel

import androidx.lifecycle.ViewModel
import com.example.gtam.MyApp

class MemoryViewModel : ViewModel() {
    private val memoryDAO = MyApp.database.memoryDao()


}