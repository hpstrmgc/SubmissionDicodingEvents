package com.nat.submissionnavigationapi

import com.google.gson.annotations.SerializedName

data class EventResponse(

    @SerializedName("error")
    val error: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("listEvents")
    val listEvents: List<ListEventsItem>
)