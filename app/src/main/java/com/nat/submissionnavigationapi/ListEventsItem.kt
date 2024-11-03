package com.nat.submissionnavigationapi

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListEventsItem(
    val id: Int,
    val name: String,
    val ownerName: String,
    val beginTime: String,
    val mediaCover: String,
    val quota: Int,
    val registrants: Int,
    val description: String,
    val link: String
): Parcelable