package com.project.premiereservice.network

class NetworkRequest {
    private val movieClient = MovieService.client

    suspend fun getMovieLists(query: String) = movieClient?.getMovies(query)
    suspend fun getUserInfo(id: String) = movieClient?.getUserInfo(id)
    suspend fun isLoginSuccess(id: String, pw: String) = movieClient?.isLoginSuccess(id, pw)
}