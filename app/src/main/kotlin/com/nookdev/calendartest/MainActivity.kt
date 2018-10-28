package com.nookdev.calendartest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nookdev.calendartest.ui.calendarlist.CalendarListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, CalendarListFragment.newInstance())
                .commitNow()
        }
    }

}
