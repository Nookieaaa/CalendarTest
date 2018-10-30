package com.nookdev.calendartest.model.alarm

import android.app.PendingIntent
import org.threeten.bp.ZonedDateTime

interface AlarmHelper {
    fun scheduleAlarm(intent: PendingIntent, time: ZonedDateTime)
}