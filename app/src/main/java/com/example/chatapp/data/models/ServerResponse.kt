package com.example.chatapp.data.models

import com.google.gson.annotations.SerializedName

data class ServerResponse(
    @SerializedName("server")
    val status: String
)