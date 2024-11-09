package com.nat.submissionnavigationapi.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.nat.submissionnavigationapi.R
import com.nat.submissionnavigationapi.database.FavoriteEvent
import com.nat.submissionnavigationapi.databinding.ActivityDetailEventBinding
import com.nat.submissionnavigationapi.repository.ViewModelFactory
import com.nat.submissionnavigationapi.resource.ApiConfig
import com.nat.submissionnavigationapi.resource.EventResponse
import com.nat.submissionnavigationapi.ui.favorite.FavoriteViewModel
import com.nat.submissionnavigationapi.ui.settings.SettingsPreferences
import com.nat.submissionnavigationapi.ui.settings.SettingsViewModel
import com.nat.submissionnavigationapi.ui.settings.SettingsViewModelFactory
import com.nat.submissionnavigationapi.ui.settings.dataStore
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailEventBinding
    private var isFavorite = false
    private var currentEvent: ListEventsItem? = null

    private val favoriteViewModel: FavoriteViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }

    private val settingsViewModel: SettingsViewModel by viewModels {
        val pref = SettingsPreferences.getInstance(dataStore)
        SettingsViewModelFactory(pref)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val eventId = intent.getIntExtra("event_id", -1)
        if (eventId != -1) {
            fetchEventDetails(eventId)
        } else {
            Log.e("DetailEventActivity", "Event ID is invalid")
            showError("Invalid event ID")
        }

        binding.fabFavorite.setOnClickListener {
            currentEvent?.let { event ->
                val favoriteEvent = FavoriteEvent(
                    id = event.id,
                    name = event.name,
                    beginTime = event.beginTime,
                    mediaCover = event.mediaCover
                )
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

        settingsViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun fetchEventDetails(eventId: Int) {
        binding.progressBar.visibility = View.VISIBLE

        val apiService = ApiConfig.getApiService()
        val call: Call<EventResponse> = apiService.getEventDetails(eventId)
        call.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                binding.progressBar.visibility = View.GONE

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
                binding.progressBar.visibility = View.GONE
                Log.e("DetailEventActivity", "Failed to fetch event details: ${t.message}")
                showError("Failed to fetch event details: ${t.message}")
            }
        })
    }

    private fun displayEventDetails(event: ListEventsItem) {
        Glide.with(this).load(event.mediaCover).into(binding.imageEventCover)
        binding.textEventName.text = event.name
        binding.textOwnerName.text = event.ownerName
        binding.textEventTime.text = event.beginTime

        val remainingQuota = event.quota - event.registrants
        binding.textQuota.text = getString(R.string.remaining_quota, remainingQuota)

        binding.textDescription.text = Html.fromHtml(event.description, Html.FROM_HTML_MODE_COMPACT)

        binding.buttonOpenLink.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
            startActivity(browserIntent)
        }

        checkIfFavorite(event)
    }

    private fun checkIfFavorite(event: ListEventsItem) {
        favoriteViewModel.favoriteEvents.observe(this) { favoriteEvents ->
            isFavorite = favoriteEvents.any { it.id == event.id }
            setFavoriteIcon()
        }
    }

    private fun setFavoriteIcon() {
        binding.fabFavorite.setImageResource(
            if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
        )
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}