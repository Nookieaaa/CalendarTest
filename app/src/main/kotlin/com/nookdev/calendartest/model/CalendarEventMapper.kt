package com.nookdev.calendartest.model

import android.database.Cursor
import android.provider.CalendarContract
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId

class CalendarEventMapper : Mapper<Cursor, List<CalendarEvent>> {
    override suspend fun map(item: Cursor): List<CalendarEvent> = GlobalScope.async {
        ArrayList<CalendarEvent>().apply {
            while (item.moveToNext()) {
                add(
                    CalendarEvent(
                        id = item.getString(item.getColumnIndex("_id")),
                        title = item.getString(item.getColumnIndex(CalendarContract.Events.TITLE)),
                        startTime = Instant.ofEpochMilli(item.getLong(item.getColumnIndex(CalendarContract.Events.DTSTART))).atZone(
                            ZoneId.systemDefault()
                        ),
                        endTime = Instant.ofEpochMilli(item.getLong(item.getColumnIndex(CalendarContract.Events.DTEND))).atZone(
                            ZoneId.systemDefault()
                        )
                    )
                )
            }
        }
    }.await()

}