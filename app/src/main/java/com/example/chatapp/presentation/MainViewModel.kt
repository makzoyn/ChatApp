package com.example.chatapp.presentation

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

    fun receiveMessage() {
        viewModelScope.launch {
            ApiFactory.apiService.receiveMessage().let {
                if (it.isSuccessful) {
                    val message = it.body()
                    Log.d("message", message.toString())
                    if (message != null) {
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
    }

    companion object {
        private const val SENDER = "sender"
    }
}