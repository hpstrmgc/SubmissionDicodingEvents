package com.nat.submissionnavigationapi

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class UpcomingEventsAdapter(private val eventsList: List<ListEventsItem>) : RecyclerView.Adapter<UpcomingEventsAdapter.EventViewHolder>() {

    @Suppress("DEPRECATION")
    inner class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val eventName: TextView = view.findViewById(R.id.tv_event_name)
        val eventImage: ImageView = view.findViewById(R.id.iv_event_image)
        val eventDate: TextView = view.findViewById(R.id.tv_event_date) // Menambahkan TextView untuk tanggal

        init {
            // Menangani klik item
            view.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val event = eventsList[position]
                    // Membuat intent untuk DetailEventActivity
                    val intent = Intent(view.context, DetailEventActivity::class.java).apply {
                        putExtra("event_id", event.id) // Mengirim ID event
                    }
                    view.context.startActivity(intent) // Memulai DetailEventActivity
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_upcoming_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = eventsList[position]
        holder.eventName.text = event.name

        // Menggunakan Glide untuk memuat gambar
        Glide.with(holder.itemView.context)
            .load(event.mediaCover)
            .apply(RequestOptions()
                .placeholder(android.R.color.darker_gray)
                .error(android.R.drawable.stat_notify_error))
            .into(holder.eventImage)

        // Mengikat tanggal ke TextView
        holder.eventDate.text = event.beginTime // Menampilkan tanggal
    }

    override fun getItemCount(): Int {
        return eventsList.size.coerceAtMost(5) // Tampilkan maksimal 5 item
    }
}
