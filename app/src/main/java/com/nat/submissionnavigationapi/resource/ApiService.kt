package com.nat.submissionnavigationapi.resource

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    fun getUpcomingEvents(
        @Query("active") active: Int = 1
    ): Call<EventResponse>

    @GET("events")
    fun getFinishedEvents(
        @Query("active") active: Int = 0
    ): Call<EventResponse>

    @GET("events")
    fun getEventDetails(@Query("id") eventId: Int): Call<EventResponse>
}
