package com.project.premiereservice.network

import com.project.premiereservice.model.MoviesModel
import com.project.premiereservice.model.UserModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {
    @GET("/")
    suspend fun getMovies(
        @Query("q") query: String
    ): Response<MoviesModel>

    @GET("/userInfo")
    suspend fun getUserInfo(
            @Query("q") query: String
    ): Response<UserModel>
}