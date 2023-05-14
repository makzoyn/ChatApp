package com.example.chatapp.data.models

import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("id_of_mess")
    val id: Int,
    @SerializedName("message")
    val text: String,
    @SerializedName("toname")
    val recipient: String
)