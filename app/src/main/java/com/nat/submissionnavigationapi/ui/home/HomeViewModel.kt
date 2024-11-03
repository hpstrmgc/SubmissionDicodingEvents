package com.nat.submissionnavigationapi.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nat.submissionnavigationapi.resource.ApiConfig
import com.nat.submissionnavigationapi.resource.EventResponse
import com.nat.submissionnavigationapi.ui.detail.ListEventsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _events = MutableLiveData<List<ListEventsItem>>()
    val events: LiveData<List<ListEventsItem>> = _events

    private val _finishedEvents = MutableLiveData<List<ListEventsItem>>()
    val finishedEvents: LiveData<List<ListEventsItem>> = _finishedEvents

    private val _errorState = MutableLiveData<String?>()
    val errorState: LiveData<String?> = _errorState

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        fetchEvents()
        fetchFinishedEvents()
    }

    private fun fetchEvents() {
        _isLoading.value = true
        val apiService = ApiConfig.getApiService()
        apiService.getUpcomingEvents().enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false

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
                _isLoading.value = false
                _errorState.value = "Failure: ${t.message}"
                Log.d("HomeViewModel", "Failure: ${t.message}")
            }
        })
    }

    private fun fetchFinishedEvents() {
        _isLoading.value = true
        val apiService = ApiConfig.getApiService()
        apiService.getFinishedEvents().enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    response.body()?.let {
                        _finishedEvents.value = it.listEvents
                    } ?: run {
                        _errorState.value = "Response body is null"
                    }
                } else {
                    _errorState.value = handleError(response.code(), response.message())
                    Log.d("HomeViewModel", "Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                _errorState.value = "Failure: ${t.message}"
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
        _errorState.value = null
    }
}
