package com.project.premiereservice.network

class NetworkRequest {
    private val movieClient = MovieService.client

    suspend fun getMovieLists(id: String) = movieClient?.getMovies(id)
    suspend fun getUserInfo(id: String) = movieClient?.getUserInfo(id)
    suspend fun isLoginSuccess(id: String, pw: String) = movieClient?.isLoginSuccess(id, pw)
    suspend fun getRegisteredMovieLists(id: Int) = movieClient?.getRegisteredMovies(id)
    suspend fun getMovieInfo(id: Long) = movieClient?.getMovieInfo(id)
    suspend fun registerMovie(movieId: Long, userId: Long, isRegister: Boolean) = movieClient?.registerMovie(movieId, userId, isRegister)
    suspend fun isMovieRegistered(movieId: Long, userId: Long) = movieClient?.isMovieRegistered(movieId, userId)
}