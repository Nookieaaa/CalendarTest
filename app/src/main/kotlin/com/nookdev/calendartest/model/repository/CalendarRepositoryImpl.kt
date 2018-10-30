package com.nookdev.calendartest.model.repository

import android.Manifest
import android.content.Context
import android.provider.CalendarContract
import androidx.annotation.RequiresPermission
import com.nookdev.calendartest.model.CalendarEvent
import com.nookdev.calendartest.model.CalendarEventMapper
import org.threeten.bp.ZonedDateTime


class CalendarRepositoryImpl(context: Context) : CalendarRepository {

    private val calendarMapper = CalendarEventMapper()
    private val contentResolver = context.contentResolver
    private val calendarUri = CalendarContract.Events.CONTENT_URI


    @RequiresPermission(Manifest.permission.READ_CALENDAR)
    override suspend fun getCalendarEvents(): List<CalendarEvent> {
        val projection = arrayOf(
            "_id",
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND
        )

        val selection =
            "((${CalendarContract.Events.DTSTART} >= ${ZonedDateTime.now().toInstant().toEpochMilli()}) " +
                    "AND" +
                    " (${CalendarContract.Events.DTSTART} <= ${ZonedDateTime.now().plusHours(1).toInstant().toEpochMilli()}))"

        val cursor = contentResolver.query(calendarUri, projection, selection, null, "_id DESC") ?: return emptyList()
        return calendarMapper.map(cursor)
            .also {
                cursor.close()
            }
    }

    @RequiresPermission(Manifest.permission.WRITE_CALENDAR)
    override suspend fun removeEvent(event: CalendarEvent) {
        val uri = CalendarContract.Events.CONTENT_URI
        val selection = "_id = ?"
        val selectionArgs = arrayOf(event.id)
        contentResolver.delete(uri, selection, selectionArgs)
    }
}