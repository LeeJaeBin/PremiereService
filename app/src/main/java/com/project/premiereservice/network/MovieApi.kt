package com.project.premiereservice.network

import com.project.premiereservice.model.LoginModel
import com.project.premiereservice.model.MoviesModel
import com.project.premiereservice.model.UserModel
import com.project.premiereservice.model.UserModelClass
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MovieApi {
    @GET("index.php")
    suspend fun getMovies(
        @Query("q") query: String
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
}