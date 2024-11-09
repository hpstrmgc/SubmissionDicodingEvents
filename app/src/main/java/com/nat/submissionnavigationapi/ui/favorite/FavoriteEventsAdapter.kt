package com.nat.submissionnavigationapi.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nat.submissionnavigationapi.database.FavoriteEvent
import com.nat.submissionnavigationapi.databinding.ItemFavoriteEventBinding

class FavoriteEventsAdapter(
    private var favoriteEvents: List<FavoriteEvent> = emptyList()
) : RecyclerView.Adapter<FavoriteEventsAdapter.FavoriteEventViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setEvents(newEvents: List<FavoriteEvent>) {
        favoriteEvents = newEvents
        notifyDataSetChanged()
    }

    fun setOnItemClickCallback(callback: OnItemClickCallback) {
        onItemClickCallback = callback
    }

    inner class FavoriteEventViewHolder(private val binding: ItemFavoriteEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event: FavoriteEvent) {
            binding.tvEventName.text = event.name
            binding.tvEventDate.text = event.beginTime

            Glide.with(itemView.context).load(event.mediaCover).apply(
                RequestOptions().placeholder(android.R.color.darker_gray)
                    .error(android.R.drawable.stat_notify_error)
            ).into(binding.ivEventImage)

            itemView.setOnClickListener {
                onItemClickCallback?.onItemClicked(event)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteEventViewHolder {
        val binding = ItemFavoriteEventBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FavoriteEventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteEventViewHolder, position: Int) {
        holder.bind(favoriteEvents[position])
    }

    override fun getItemCount(): Int = favoriteEvents.size

    interface OnItemClickCallback {
        fun onItemClicked(data: FavoriteEvent)
    }
}
