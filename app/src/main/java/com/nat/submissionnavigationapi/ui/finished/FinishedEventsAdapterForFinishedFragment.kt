package com.nat.submissionnavigationapi.ui.finished

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nat.submissionnavigationapi.databinding.ItemFinishedEventForFinishedFragmentBinding
import com.nat.submissionnavigationapi.ui.detail.ListEventsItem

class FinishedEventsAdapterForFinishedFragment(private val eventsList: List<ListEventsItem>) :
    RecyclerView.Adapter<FinishedEventsAdapterForFinishedFragment.FinishedEventViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class FinishedEventViewHolder(private val binding: ItemFinishedEventForFinishedFragmentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event: ListEventsItem) {
            binding.tvEventName.text = event.name
            binding.tvEventDate.text = event.beginTime

            Glide.with(itemView.context).load(event.mediaCover).apply(
                RequestOptions().placeholder(android.R.color.darker_gray)
                    .error(android.R.drawable.stat_notify_error)
            ).into(binding.ivEventImage)

            itemView.setOnClickListener { onItemClickCallback.onItemClicked(event) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinishedEventViewHolder {
        val binding = ItemFinishedEventForFinishedFragmentBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FinishedEventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FinishedEventViewHolder, position: Int) {
        holder.bind(eventsList[position])
    }

    override fun getItemCount(): Int {
        return eventsList.size
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ListEventsItem)
    }
}