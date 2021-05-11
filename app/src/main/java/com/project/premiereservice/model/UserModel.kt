package com.project.premiereservice.model

import com.google.gson.annotations.SerializedName

data class UserModelClass(
        @SerializedName("item")
        val item: UserModel
)

data class UserModel(
        @SerializedName("nickname")
        val nickname: String,

        @SerializedName("phonenum")
        val phoneNum: String,

        @SerializedName("imei")
        val imei: String,

        @SerializedName("category")
        val category: String,

        @SerializedName("image")
        val image: String,
)
