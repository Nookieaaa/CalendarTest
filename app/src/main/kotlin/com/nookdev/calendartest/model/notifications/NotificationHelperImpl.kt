package com.nookdev.calendartest.model.notifications

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.nookdev.calendartest.MainActivity
import com.nookdev.calendartest.R
import com.nookdev.calendartest.ZDT_SERIALIZER
import com.nookdev.calendartest.model.CalendarEvent
import com.nookdev.calendartest.model.receiver.CalendarReminderReceiver
import org.threeten.bp.ZonedDateTime
import java.util.concurrent.atomic.AtomicInteger


class NotificationHelperImpl(
    private val context: Context
) : NotificationHelper {

    init {
        configureChannels()
    }

    private val notificationManager: NotificationManager
        get() = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @TargetApi(Build.VERSION_CODES.O)
    override fun configureChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(
                notificationManager,
                CHANNEL_ID_REMINDERS,
                context.getString(R.string.notification_channel_reminder),
                NotificationManagerCompat.IMPORTANCE_DEFAULT
            )
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createChannel(
        notificationManager: NotificationManager,
        channelId: String,
        channelName: String,
        importance: Int
    ) {
        val channel = NotificationChannel(channelId, channelName, importance)
        channel.description = channelName
        notificationManager.createNotificationChannel(channel)
    }

    override fun buildDefaultPriorityNotificationTemplate(
        channelId: String,
        title: String,
        description: String
    ): NotificationCompat.Builder = NotificationCompat.Builder(context, channelId)
        .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle(title)
        .setTicker(title)
        .setContentText(description)
        .setChannelId(channelId)


    private val requestCodeCounter: AtomicInteger = AtomicInteger(0)

    override fun buildPendingIntent(event: CalendarEvent): PendingIntent {
        val intent = MainActivity.buildIntent(context, event)
        return PendingIntent.getActivity(
            context,
            requestCodeCounter.incrementAndGet(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    override fun buildScheduledPendingIntent(event: CalendarEvent): PendingIntent {
        val intent = Intent(context, CalendarReminderReceiver::class.java)
        val type = object : TypeToken<ZonedDateTime>() {}.type
        val gson = GsonBuilder()
            .registerTypeAdapter(type, ZDT_SERIALIZER)
            .create()
        intent.putExtra(EXTRA_EVENT, gson.toJson(event))
        return PendingIntent.getBroadcast(
            context,
            requestCodeCounter.incrementAndGet(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    override fun postNotification(notification: Notification, id: Int) {
        notificationManager.notify(id, notification)
    }

    override fun clearNotifications() {
        notificationManager.cancelAll()
    }

    companion object {
        const val CHANNEL_ID_REMINDERS = "REMINDER"
        const val EXTRA_EVENT = "calendar_event"
    }
}
