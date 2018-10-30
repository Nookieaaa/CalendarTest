package com.nookdev.calendartest.ui.calendarlist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.nookdev.calendartest.MainActivity
import com.nookdev.calendartest.R
import com.nookdev.calendartest.model.alarm.AlarmHelperImpl
import com.nookdev.calendartest.model.notifications.NotificationHelper
import kotlinx.android.synthetic.main.calendar_list_fragment.*

class CalendarListFragment : Fragment() {

    private val eventsAdapter = CalendarListAdapter()
    private val alarmHelper by lazy {
        AlarmHelperImpl(context ?: throw NullPointerException("Context is null"))
    }

    private lateinit var viewModel: CalendarListViewModel
    private lateinit var notificationHelper: NotificationHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.calendar_list_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CalendarListViewModel::class.java)

        with(calendarList) {
            layoutManager = LinearLayoutManager(context)
            adapter = eventsAdapter
        }
        viewModel.run {
            refresh()
            getCalendarLiveData().observe(this@CalendarListFragment, Observer {
                calendarStrl.isRefreshing = false
                eventsAdapter.setData(it)
                if (it.isNotEmpty()) {
                    calendarList.scrollToPosition(0)
                }
                it.forEach { event ->
                    val intent = notificationHelper.buildScheduledPendingIntent(event)
                    alarmHelper.scheduleAlarm(intent, event.startTime)
                }
            })
        }
        calendarStrl.setOnRefreshListener {
            viewModel.refresh()
        }
        eventsAdapter.setOnEventCancelledCallback {
            viewModel.removeEvent(it)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        notificationHelper = (context as? MainActivity)?.notificationHelper ?:
                throw IllegalArgumentException("Cannot retrieve notification helper")
    }

    companion object {
        fun newInstance() = CalendarListFragment()
    }

}
