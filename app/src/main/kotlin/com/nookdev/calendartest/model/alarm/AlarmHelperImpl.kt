package com.nookdev.calendartest.model.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import org.threeten.bp.ZonedDateTime
import timber.log.Timber

class AlarmHelperImpl(
    context: Context
) : AlarmHelper {

    private val alarmManager by lazy {
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    override fun scheduleAlarm(intent: PendingIntent, time: ZonedDateTime) {
        try {
            cancelAlarm(intent)
        } catch (e: Exception) {
            Timber.e(e, "Alarm wasn`t cancelled")
        }
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            time.toInstant().toEpochMilli(),
            intent
        )
    }

    private fun cancelAlarm(intent: PendingIntent) {
        alarmManager.cancel(intent)
    }
}