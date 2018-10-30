package com.nookdev.calendartest

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nookdev.calendartest.model.CalendarEvent
import com.nookdev.calendartest.model.notifications.NotificationHelper
import com.nookdev.calendartest.model.notifications.NotificationHelperImpl
import com.nookdev.calendartest.ui.calendarlist.CalendarListFragment
import com.nookdev.calendartest.ui.details.EventDetailsFragment
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber


class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    val notificationHelper: NotificationHelper by lazy {
        NotificationHelperImpl(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        val perms = arrayOf(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR)
        if (EasyPermissions.hasPermissions(this, *perms)) {
            navigateForward(intent)
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this, getString(R.string.calendar_rationale),
                RC_CALENDAR, *perms
            )
        }
    }

    override fun onStart() {
        super.onStart()
        notificationHelper.clearNotifications()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        navigateForward(intent)
    }

    private fun navigateForward(intent: Intent) {
        if (intent.hasExtra(EXTRA_EVENT)) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, EventDetailsFragment.newInstance(intent.getParcelableExtra(EXTRA_EVENT)))
                .commitNow()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, CalendarListFragment.newInstance())
                .commitNow()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        Timber.d("Permissions denied, exiting")
        finish()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        navigateForward(intent)
    }

    companion object {
        private const val RC_CALENDAR = 101
        private const val EXTRA_EVENT = "calendar_event"

        fun buildIntent(context: Context, event: CalendarEvent? = null) =
            Intent(context, MainActivity::class.java).apply {
                if (event != null) {
                    putExtra(EXTRA_EVENT, event)
                }
            }
    }

}
