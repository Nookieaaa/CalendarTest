package com.nookdev.calendartest.ui.calendarlist

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.nookdev.calendartest.model.CalendarEvent
import com.nookdev.calendartest.model.repository.CalendarRepository
import com.nookdev.calendartest.model.repository.CalendarRepositoryImpl
import com.nookdev.calendartest.ui.BaseViewModel
import timber.log.Timber

class CalendarListViewModel(application: Application) : BaseViewModel(application) {

    private val calendarRepository: CalendarRepository = CalendarRepositoryImpl(application)
    private val liveData = MutableLiveData<List<CalendarEvent>>()

    fun getCalendarLiveData() = liveData

    fun refresh() = launchOnUiScope {
        try {
            val events = calendarRepository.getCalendarEvents()
            liveData.postValue(events)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    fun removeEvent(event: CalendarEvent) = launchOnUiScope {
        calendarRepository.removeEvent(event)
        refresh()
    }

}
