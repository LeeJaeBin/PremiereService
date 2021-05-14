package com.project.premiereservice.model

import com.google.gson.annotations.SerializedName

data class CheckMovieRegisteredModel(
        @SerializedName("isRegistered")
        val checkRegistered: Boolean
)
