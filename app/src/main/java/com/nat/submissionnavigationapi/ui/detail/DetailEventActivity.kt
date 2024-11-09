package com.nat.submissionnavigationapi.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.nat.submissionnavigationapi.R
import com.nat.submissionnavigationapi.database.FavoriteEvent
import com.nat.submissionnavigationapi.repository.ViewModelFactory
import com.nat.submissionnavigationapi.resource.ApiConfig
import com.nat.submissionnavigationapi.resource.EventResponse
import com.nat.submissionnavigationapi.ui.favorite.FavoriteViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailEventActivity : AppCompatActivity() {
    private lateinit var imageEventCover: ImageView
    private lateinit var textEventName: TextView
    private lateinit var textOwnerName: TextView
    private lateinit var textEventTime: TextView
    private lateinit var textQuota: TextView
    private lateinit var textDescription: TextView
    private lateinit var buttonOpenLink: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var fabFavorite: FloatingActionButton

    private var isFavorite = false
    private var currentEvent: ListEventsItem? = null

    private val favoriteViewModel: FavoriteViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_detail_event)

        imageEventCover = findViewById(R.id.image_event_cover)
        textEventName = findViewById(R.id.text_event_name)
        textOwnerName = findViewById(R.id.text_owner_name)
        textEventTime = findViewById(R.id.text_event_time)
        textQuota = findViewById(R.id.text_quota)
        textDescription = findViewById(R.id.text_description)
        buttonOpenLink = findViewById(R.id.button_open_link)
        progressBar = findViewById(R.id.progress_bar)
        fabFavorite = findViewById(R.id.fab_favorite)

        val eventId = intent.getIntExtra("event_id", -1)
        if (eventId != -1) {
            fetchEventDetails(eventId)
        } else {
            Log.e("DetailEventActivity", "Event ID is invalid")
            showError("Invalid event ID")
        }

        fabFavorite.setOnClickListener {
            currentEvent?.let { event ->
                val favoriteEvent = FavoriteEvent(
                    id = event.id,
                    name = event.name,
                    beginTime = event.beginTime,
                    mediaCover = event.mediaCover
                )
                // Panggil addOrRemoveFavorite() dari coroutine
                lifecycleScope.launch {
                    favoriteViewModel.addOrRemoveFavorite(favoriteEvent)
                }
            }
        }

        favoriteViewModel.favoriteEvents.observe(this) { favoriteEvents ->
            currentEvent?.let { event ->
                isFavorite = favoriteEvents.any { it.id == event.id }
                setFavoriteIcon()
            }
        }
    }

    private fun fetchEventDetails(eventId: Int) {
        progressBar.visibility = View.VISIBLE

        val apiService = ApiConfig.getApiService()
        val call: Call<EventResponse> = apiService.getEventDetails(eventId)
        call.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                progressBar.visibility = View.GONE

                if (response.isSuccessful && response.body() != null) {
                    val event = response.body()?.listEvents?.firstOrNull { it.id == eventId }
                    if (event != null) {
                        displayEventDetails(event)
                        currentEvent = event
                    } else {
                        Log.e("DetailEventActivity", "Event not found in the response")
                        showError("Event not found")
                    }
                } else {
                    Log.e("DetailEventActivity", "Response was not successful or body was null")
                    showError("Failed to fetch event details")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                Log.e("DetailEventActivity", "Failed to fetch event details: ${t.message}")
                showError("Failed to fetch event details: ${t.message}")
            }
        })
    }

    private fun displayEventDetails(event: ListEventsItem) {
        Glide.with(this).load(event.mediaCover).into(imageEventCover)
        textEventName.text = event.name
        textOwnerName.text = event.ownerName
        textEventTime.text = event.beginTime

        val remainingQuota = event.quota - event.registrants
        textQuota.text = getString(R.string.remaining_quota, remainingQuota)

        textDescription.text = Html.fromHtml(event.description, Html.FROM_HTML_MODE_COMPACT)

        buttonOpenLink.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
            startActivity(browserIntent)
        }

        // Pastikan status favorit diperbarui setelah event ditampilkan
        checkIfFavorite(event)
    }

    private fun checkIfFavorite(event: ListEventsItem) {
        favoriteViewModel.favoriteEvents.observe(this) { favoriteEvents ->
            isFavorite = favoriteEvents.any { it.id == event.id }
            setFavoriteIcon()
        }
    }

    private fun setFavoriteIcon() {
        fabFavorite.setImageResource(
            if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
        )
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
