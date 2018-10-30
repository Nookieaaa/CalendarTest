package com.nookdev.calendartest.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nookdev.calendartest.R
import com.nookdev.calendartest.model.CalendarEvent
import kotlinx.android.synthetic.main.event_details_fragment.*

class EventDetailsFragment : Fragment() {

    private val event: CalendarEvent
        get() = arguments?.getParcelable(ARG_EVENT) ?: throw IllegalArgumentException("event is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.event_details_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        eventTitle.text = event.title
        eventTime.text = event.time()
    }

    companion object {
        private const val ARG_EVENT = "event"

        fun newInstance(event: CalendarEvent) = EventDetailsFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_EVENT, event)
            }
        }
    }
}