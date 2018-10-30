package com.nookdev.calendartest.model.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.nookdev.calendartest.ZDT_DESERIALIZER
import com.nookdev.calendartest.model.CalendarEvent
import com.nookdev.calendartest.model.notifications.NotificationHelper
import com.nookdev.calendartest.model.notifications.NotificationHelperImpl
import org.threeten.bp.ZonedDateTime

class CalendarReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val notificationHelper: NotificationHelper = NotificationHelperImpl(context)
        val stringExtra = intent.getStringExtra(NotificationHelperImpl.EXTRA_EVENT)
        val type = object : TypeToken<ZonedDateTime>() {}.type
        val gson = GsonBuilder()
            .registerTypeAdapter(type, ZDT_DESERIALIZER)
            .create()
        val event: CalendarEvent = gson.fromJson(stringExtra, CalendarEvent::class.java)
        val pendingIntent = notificationHelper.buildPendingIntent(event)
        val notification = notificationHelper.buildDefaultPriorityNotificationTemplate(
            NotificationHelperImpl.CHANNEL_ID_REMINDERS,
            event.title,
            "Reminder"
        )
            .setContentIntent(pendingIntent)
            .build()
        notificationHelper.postNotification(notification, event.hashCode())
    }

}