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
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.nat.submissionnavigationapi.R
import com.nat.submissionnavigationapi.resource.ApiConfig.getApiService
import com.nat.submissionnavigationapi.resource.EventResponse
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

        val eventId = intent.getIntExtra("event_id", -1)
        if (eventId != -1) {
            fetchEventDetails(eventId)
        } else {
            Log.e("DetailEventActivity", "Event ID is invalid")
            showError("Invalid event ID")
        }
    }

    private fun fetchEventDetails(eventId: Int) {
        progressBar.visibility = View.VISIBLE

        val apiService = getApiService()
        val call: Call<EventResponse> = apiService.getEventDetails(eventId)
        call.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                progressBar.visibility = View.GONE

                if (response.isSuccessful && response.body() != null) {
                    val event = response.body()?.listEvents?.firstOrNull { it.id == eventId }
                    if (event != null) {
                        displayEventDetails(event)
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

        // Use string resource with placeholder
        val remainingQuota = event.quota - event.registrants
        textQuota.text = getString(R.string.remaining_quota, remainingQuota)

        textDescription.text = Html.fromHtml(event.description)

        buttonOpenLink.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
            startActivity(browserIntent)
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}