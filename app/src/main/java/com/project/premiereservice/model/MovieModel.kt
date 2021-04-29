package com.project.premiereservice.model

import com.google.gson.annotations.SerializedName

data class MoviesModel(
        @SerializedName("total_count")
        val totalCount: Int,

        @SerializedName("items")
        val items: List<MovieModel>
)

data class MovieModel(
        @SerializedName("id")
        val id: Long,

        @SerializedName("movie_title")
        val movieTitle: String,

        @SerializedName("movie_rank")
        val movieRank: Int,

        @SerializedName("available")
        val availableNum: Int,

        @SerializedName("movie_poster")
        val moviePoster: String,

        @SerializedName("categories")
        val movieCategory: String
)
