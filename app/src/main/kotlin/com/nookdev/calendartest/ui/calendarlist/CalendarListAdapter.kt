package com.nookdev.calendartest.ui.calendarlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nookdev.calendartest.R
import com.nookdev.calendartest.model.CalendarEvent

class CalendarListAdapter : RecyclerView.Adapter<CalendarListAdapter.ViewHolder>() {

    private val data = mutableListOf<CalendarEvent>()
    private var _onEventCancelled: (CalendarEvent) -> Unit = {}

    fun setData(newData: List<CalendarEvent>) {
        val callback = object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                data[oldItemPosition].id == newData[newItemPosition].id

            override fun getOldListSize() = data.size

            override fun getNewListSize() = newData.size

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                data[oldItemPosition] == newData[newItemPosition]
        }
        DiffUtil.calculateDiff(callback, true).dispatchUpdatesTo(this)

        with(data) {
            clear()
            addAll(newData)
        }
    }

    fun setOnEventCancelledCallback(callback: (CalendarEvent) -> Unit) {
        _onEventCancelled = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.calendar_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val title: TextView = itemView.findViewById(R.id.eventTitle)
        private val time: TextView = itemView.findViewById(R.id.eventTime)
        private val cancelBtn: Button = itemView.findViewById(R.id.eventCancel)

        fun bind(model: CalendarEvent) {
            title.text = model.title
            time.text = model.time()
            cancelBtn.setOnClickListener {
                _onEventCancelled(model)
            }
        }
    }

}