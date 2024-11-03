package com.nat.submissionnavigationapi.ui.finished

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nat.submissionnavigationapi.databinding.ItemFinishedEventBinding
import com.nat.submissionnavigationapi.ui.detail.ListEventsItem

class FinishedEventsAdapter(private val eventsList: List<ListEventsItem>) :
    RecyclerView.Adapter<FinishedEventsAdapter.EventViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class EventViewHolder(private val binding: ItemFinishedEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event: ListEventsItem) {
            binding.textEventName.text = event.name
            binding.textBeginTime.text = event.beginTime

            Glide.with(itemView.context).load(event.mediaCover).apply(
                RequestOptions().placeholder(android.R.color.darker_gray)
                    .error(android.R.drawable.stat_notify_error)
            ).into(binding.imageEvent)

            itemView.setOnClickListener { onItemClickCallback.onItemClicked(event) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding =
            ItemFinishedEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
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