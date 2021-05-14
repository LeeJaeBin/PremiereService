package com.project.premiereservice.network

import com.project.premiereservice.model.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MovieApi {
    @GET("index.php")
    suspend fun getMovies(
        @Query("id") id: String
    ): Response<MoviesModel>

    @POST("userinfo.php")
    suspend fun getUserInfo(
            @Query("id") id: String
    ): Response<UserModelClass>

    @POST("login.php")
    suspend fun isLoginSuccess(
            @Query("id") id: String,
            @Query("pw") pw: String
    ): Response<LoginModel>

    @POST("getregister.php")
    suspend fun getRegisteredMovies(
            @Query("id") id: Int
    ): Response<RegisteredMoviesModel>

    @POST("getMovieInfo.php")
    suspend fun getMovieInfo(
            @Query("id") id: Long
    ): Response<MovieInfoModels>

    @POST("registerMovie.php")
    suspend fun registerMovie(
            @Query("movieId") movieId: Long,
            @Query("userId") userId: Long,
            @Query("isRegister") isRegister: Boolean
    ): Response<LoginModel>

    @POST("isMovieRegistered.php")
    suspend fun isMovieRegistered(
            @Query("movieId") movieId: Long,
            @Query("userId") userId: Long
    ): Response<CheckMovieRegisteredModel>
}