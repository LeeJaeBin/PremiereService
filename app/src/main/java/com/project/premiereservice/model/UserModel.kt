package com.project.premiereservice.model

import com.google.gson.annotations.SerializedName

data class UserModel(
        @SerializedName("id")
        val id: String,

        @SerializedName("nickname")
        val nickname: String,

        @SerializedName("phonenum")
        val phoneNum: Int,

        @SerializedName("imei")
        val imei: Int,

        @SerializedName("category")
        val category: String,

        @SerializedName("password")
        val password: String
)
