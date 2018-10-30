package com.nookdev.calendartest.model.notifications

import android.app.Notification
import android.app.PendingIntent
import androidx.core.app.NotificationCompat
import com.nookdev.calendartest.model.CalendarEvent

interface NotificationHelper {
    fun configureChannels()

    fun buildDefaultPriorityNotificationTemplate(
        channelId: String,
        title: String,
        description: String
    ): NotificationCompat.Builder

    fun buildPendingIntent(event: CalendarEvent): PendingIntent

    fun buildScheduledPendingIntent(event: CalendarEvent): PendingIntent

    fun postNotification(notification: Notification, id: Int)

    fun clearNotifications()
}