package com.project.premiereservice.model

import com.google.gson.annotations.SerializedName

data class LoginModel(
        @SerializedName("isSuccess")
        val isSuccess: Boolean
)
