package com.nookdev.calendartest.ui.calendarlist

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nookdev.calendartest.R

class CalendarListFragment : androidx.fragment.app.Fragment() {

    companion object {
        fun newInstance() = CalendarListFragment()
    }

    private lateinit var viewModel: CalendarListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.calendar_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CalendarListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
