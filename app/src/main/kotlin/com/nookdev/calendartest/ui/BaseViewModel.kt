package com.nookdev.calendartest.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

open class BaseViewModel(application: Application) : AndroidViewModel(application) {
    private val scopeJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + scopeJob)

    fun launchOnUiScope(block: suspend () -> Unit) = uiScope.launch {
        block()
    }

    override fun onCleared() {
        super.onCleared()
        scopeJob.cancel()
    }
}