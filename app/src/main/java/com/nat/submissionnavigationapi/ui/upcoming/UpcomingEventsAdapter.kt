package com.nat.submissionnavigationapi.ui.upcoming

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nat.submissionnavigationapi.databinding.ItemUpcomingEventBinding
import com.nat.submissionnavigationapi.ui.detail.DetailEventActivity
import com.nat.submissionnavigationapi.ui.detail.ListEventsItem

class UpcomingEventsAdapter(private val eventsList: List<ListEventsItem>) :
    RecyclerView.Adapter<UpcomingEventsAdapter.EventViewHolder>() {

    inner class EventViewHolder(private val binding: ItemUpcomingEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val event = eventsList[position]
                    val intent = Intent(itemView.context, DetailEventActivity::class.java).apply {
                        putExtra("event_id", event.id)
                    }
                    itemView.context.startActivity(intent)
                }
            }
        }

        fun bind(event: ListEventsItem) {
            binding.tvEventName.text = event.name
            binding.tvEventDate.text = event.beginTime

            Glide.with(itemView.context).load(event.mediaCover).apply(
                    RequestOptions().placeholder(android.R.color.darker_gray)
                        .error(android.R.drawable.stat_notify_error)
                ).into(binding.ivEventImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding =
            ItemUpcomingEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = eventsList[position]
        holder.bind(event)
    }

    override fun getItemCount(): Int {
        return eventsList.size.coerceAtMost(5)
    }
}