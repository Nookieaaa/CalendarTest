package com.nookdev.calendartest.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.ZonedDateTime

@Parcelize
data class CalendarEvent(
    val id: String,
    val title: String,
    val startTime: ZonedDateTime,
    val endTime: ZonedDateTime
) : Parcelable {
    fun time() = "$startTime - $endTime"
}