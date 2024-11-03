package com.nat.submissionnavigationapi.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.nat.submissionnavigationapi.ApiConfig
import com.nat.submissionnavigationapi.EventResponse
import com.nat.submissionnavigationapi.ListEventsItem

class HomeViewModel : ViewModel() {

    private val _events = MutableLiveData<List<ListEventsItem>>()
    val events: LiveData<List<ListEventsItem>> = _events

    private val _finishedEvents = MutableLiveData<List<ListEventsItem>>() // Tambahkan LiveData untuk finished events
    val finishedEvents: LiveData<List<ListEventsItem>> = _finishedEvents

    private val _errorState = MutableLiveData<String?>()
    val errorState: LiveData<String?> = _errorState

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        fetchEvents()
        fetchFinishedEvents() // Panggil untuk mendapatkan finished events
    }

    private fun fetchEvents() {
        _isLoading.value = true // Tampilkan progress bar
        val apiService = ApiConfig.getApiService()
        apiService.getUpcomingEvents().enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false // Sembunyikan progress bar

                if (response.isSuccessful) {
                    response.body()?.let {
                        _events.value = it.listEvents
                    } ?: run {
                        _errorState.value = "Response body is null"
                    }
                } else {
                    _errorState.value = handleError(response.code(), response.message())
                    Log.d("HomeViewModel", "Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false // Sembunyikan progress bar
                _errorState.value = "Failure: ${t.message}" // Update error state
                Log.d("HomeViewModel", "Failure: ${t.message}")
            }
        })
    }

    private fun fetchFinishedEvents() {
        // Implementasi pemanggilan API untuk finished events
        // Misalnya, jika Anda menggunakan endpoint yang berbeda, tambahkan logika pemanggilan API di sini
        _isLoading.value = true // Tampilkan progress bar
        val apiService = ApiConfig.getApiService()
        apiService.getFinishedEvents().enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false // Sembunyikan progress bar

                if (response.isSuccessful) {
                    response.body()?.let {
                        _finishedEvents.value = it.listEvents // Asumsi data finished events berada di sini
                    } ?: run {
                        _errorState.value = "Response body is null"
                    }
                } else {
                    _errorState.value = handleError(response.code(), response.message())
                    Log.d("HomeViewModel", "Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false // Sembunyikan progress bar
                _errorState.value = "Failure: ${t.message}" // Update error state
                Log.d("HomeViewModel", "Failure: ${t.message}")
            }
        })
    }

    private fun handleError(statusCode: Int, message: String): String {
        return when (statusCode) {
            401 -> "$statusCode: Bad Request"
            403 -> "$statusCode: Forbidden"
            404 -> "$statusCode: Not Found"
            else -> "$statusCode: $message"
        }
    }

    fun clearError() {
        _errorState.value = null // Reset error state
    }
}
