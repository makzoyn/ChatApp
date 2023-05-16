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
                    if (!it.isSuccessful) {
                        Log.d("MainViewModel", "Error ${it.errorBody()}")
                    }
                }
        }
    }

    val handler = android.os.Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            viewModelScope.launch {
                ApiFactory.apiService.receiveMessage().let {
                    val message = it.body()
                    if (it.isSuccessful) {
                        if (message != null  && message.id != lastMessageId) {
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
            handler.postDelayed(this, 1000)
        }
    }

    fun getRunnable(): Runnable {
        return runnable
    }
    init {
        handler.postDelayed(runnable, 1000)
    }


}

