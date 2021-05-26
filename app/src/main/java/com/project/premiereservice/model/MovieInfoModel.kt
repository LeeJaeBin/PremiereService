package com.project.premiereservice.model

import com.google.gson.annotations.SerializedName

data class MovieInfoModels(

        @SerializedName("item")
        val item: MovieInfoModel
)

data class MovieInfoModel(
        @SerializedName("id")
        val id: Long,

        @SerializedName("title")
        val movieTitle: String,

        @SerializedName("available")
        val available: Int,

        @SerializedName("showDate")
        val showDate: String,

        @SerializedName("limitDate")
        val limitDate: String,

        @SerializedName("likes")
        val likes: Int,

        @SerializedName("director")
        val director: String,

        @SerializedName("actor")
        val actor: String,

        @SerializedName("descriptionTxt")
        val descriptionTxt: String,

        @SerializedName("categories")
        val categories: String,

        @SerializedName("poster")
        val poster: String,

        @SerializedName("movieurl")
        val movieUrl: String
)
