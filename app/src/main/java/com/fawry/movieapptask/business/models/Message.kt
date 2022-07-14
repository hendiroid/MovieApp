package com.fawry.movieapptask.business.models

import com.google.gson.annotations.SerializedName

data class Message(
    val success: Boolean = false,
    @SerializedName("status_message")
    val message: String = ""
)