package com.nookdev.calendartest.model.repository

import com.nookdev.calendartest.model.CalendarEvent

interface CalendarRepository {
    suspend fun getCalendarEvents(): List<CalendarEvent>
    suspend fun removeEvent(event: CalendarEvent)
}