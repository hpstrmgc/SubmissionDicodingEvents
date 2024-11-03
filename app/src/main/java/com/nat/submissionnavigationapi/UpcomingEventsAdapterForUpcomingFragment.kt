package com.nat.submissionnavigationapi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class UpcomingEventsAdapterForUpcomingFragment(private val eventsList: List<ListEventsItem>) :
    RecyclerView.Adapter<UpcomingEventsAdapterForUpcomingFragment.EventViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val eventName: TextView = view.findViewById(R.id.tv_event_name)
        private val eventImage: ImageView = view.findViewById(R.id.iv_event_image)
        private val eventDate: TextView = view.findViewById(R.id.tv_event_date)

        fun bind(event: ListEventsItem) {
            eventName.text = event.name
            eventDate.text = event.beginTime

            Glide.with(itemView.context)
                .load(event.mediaCover)
                .apply(
                    RequestOptions()
                        .placeholder(android.R.color.darker_gray)
                        .error(android.R.drawable.stat_notify_error)
                )
                .into(eventImage)

            itemView.setOnClickListener { onItemClickCallback.onItemClicked(event) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_upcoming_event_for_upcoming_fragment, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(eventsList[position])
    }

    override fun getItemCount(): Int {
        return eventsList.size.coerceAtMost(5)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ListEventsItem)
    }
}