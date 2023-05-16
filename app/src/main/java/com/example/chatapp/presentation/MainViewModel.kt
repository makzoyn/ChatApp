package com.example.chatapp.presentation

import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.models.Message
import com.example.chatapp.data.network.ApiFactory
import kotlinx.coroutines.launch


class MainViewModel : ViewModel() {
    private val _messages = MutableLiveData<List<Message>>()

    private val messageList = mutableListOf<Message>()
    private var lastMessageId: Int? = null
    companion object {
        private const val SENDER = "sender"
    }
    val messages: LiveData<List<Message>>
        get() = _messages

    fun sendMessage(message: Message) {
        viewModelScope.launch {
            ApiFactory
                .apiService
                .sendMessage(SENDER, message.recipient, message.text).let {
                    if (it.isSuccessful) {
                        messageList.add(message)
                        _messages.postValue(messageList.toList())
                    } else {
                        Log.d("MainViewModel", "Error ${it.errorBody()}")
                    }
                }
        }
    }

    val handler = android.os.Handler(Looper.getMainLooper())
    val runnable = object : Runnable {
        override fun run() {
            viewModelScope.launch {
                ApiFactory.apiService.receiveMessage().let {
                    if (it.isSuccessful) {
                        val message = it.body()
                        if (message != null && message.id != lastMessageId) {
                            lastMessageId = message.id
                            messageList.add(message)
                        }
                        Log.d("messageList", messageList.toString())
                        _messages.value = messageList
                        Log.d("_message", _messages.value.toString())
                    } else {
                        Log.d("MainViewModel", "Error ${it.errorBody()}")
                    }
                }
            }
            handler.postDelayed(this, 5000)
        }
    }

    init {
        handler.postDelayed(runnable, 5000)
    }
}

