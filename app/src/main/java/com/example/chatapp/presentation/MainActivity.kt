package com.example.chatapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.R
import com.example.chatapp.data.models.Message
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var rvMessages: RecyclerView
    private lateinit var btnSend: FloatingActionButton
    private lateinit var etInputMessage: EditText

    private lateinit var adapter: MessagesAdapter

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        adapter = MessagesAdapter()
        rvMessages.adapter = adapter

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.messages.observe(this) {
            adapter.messages = it
        }

        setClickListeners()
        viewModel.getRunnable()
    }

    private fun initViews() {
        rvMessages = findViewById(R.id.rvMessages)
        btnSend = findViewById(R.id.btnSend)
        etInputMessage = findViewById(R.id.etInputMessage)
    }

    private fun setClickListeners() {

        btnSend.setOnClickListener {
            val text = etInputMessage.text.toString()
            val message = Message(ID, text, RECIPIENT)
            viewModel.sendMessage(message)
            etInputMessage.text.clear()
        }

    }

    companion object {
        private const val RECIPIENT = "maks"
        private const val ID = 0
    }
}

