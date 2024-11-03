package com.nat.submissionnavigationapi.resource

import com.google.gson.annotations.SerializedName
import com.nat.submissionnavigationapi.ui.detail.ListEventsItem

data class EventResponse(

    @SerializedName("error")
    val error: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("listEvents")
    val listEvents: List<ListEventsItem>
)