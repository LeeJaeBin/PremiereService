package com.project.premiereservice.network

class MovieLists {
    private val movieClient = MovieService.client

    suspend fun getMovieLists(query: String) = movieClient?.getMovies(query)
    suspend fun getUserInfo(query: String) = movieClient?.getUserInfo(query)
}