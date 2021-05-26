package com.project.premiereservice.model

import com.google.gson.annotations.SerializedName

data class RegisteredMoviesModel(
        @SerializedName("items")
        val items: List<RegisteredMovieModel>
)

data class RegisteredMovieModel(
        @SerializedName("id")
        val id: Long,

        @SerializedName("poster")
        val moviePoster: String,

        @SerializedName("title")
        val title: String,

        @SerializedName("showDate")
        val showDate: String,

        @SerializedName("isSuccess")
        val isSuccess: Boolean,

        @SerializedName("limitDate")
        val limitDate: String
)
