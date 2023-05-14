package com.example.chatapp.data.network

import com.example.chatapp.data.models.Message
import com.example.chatapp.data.models.ServerResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("chat")
    suspend fun receiveMessage(): Response<Message>

    @GET("send/{sender}/{recipient}/{message}")
    suspend fun sendMessage(
        @Path("sender") sender: String,
        @Path("recipient") recipient: String,
        @Path("message") message: String
    ): Response<ServerResponse>
}