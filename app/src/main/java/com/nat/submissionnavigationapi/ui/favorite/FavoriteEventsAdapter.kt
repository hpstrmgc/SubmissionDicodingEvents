package com.nat.submissionnavigationapi.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nat.submissionnavigationapi.database.FavoriteEvent
import com.nat.submissionnavigationapi.databinding.ItemFavoriteEventBinding

class FavoriteEventsAdapter(
    private var favoriteEvents: List<FavoriteEvent> = emptyList() // Inisialisasi dengan list kosong
) : RecyclerView.Adapter<FavoriteEventsAdapter.FavoriteEventViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null // Gunakan nullable untuk menangani saat belum di-set

    fun setEvents(newEvents: List<FavoriteEvent>) {
        favoriteEvents = newEvents
        notifyDataSetChanged() // Beri tahu adapter bahwa data telah berubah
    }

    fun setOnItemClickCallback(callback: OnItemClickCallback) {
        onItemClickCallback = callback // Set callback
    }

    inner class FavoriteEventViewHolder(private val binding: ItemFavoriteEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event: FavoriteEvent) {
            binding.tvEventName.text = event.name
            binding.tvEventDate.text = event.beginTime

            Glide.with(itemView.context)
                .load(event.mediaCover)
                .apply(
                    RequestOptions()
                        .placeholder(android.R.color.darker_gray)
                        .error(android.R.drawable.stat_notify_error)
                )
                .into(binding.ivEventImage) // Pastikan ini sesuai dengan ID di layout Anda

            // Set listener untuk klik item
            itemView.setOnClickListener {
                onItemClickCallback?.onItemClicked(event) // Gunakan safe call
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteEventViewHolder {
        val binding = ItemFavoriteEventBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
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
