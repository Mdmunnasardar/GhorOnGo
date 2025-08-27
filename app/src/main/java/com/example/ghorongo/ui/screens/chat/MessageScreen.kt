package com.example.ghorongo.ui.screens.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color

// Data model for UI
data class Message(
    val text: String,
    val isSender: Boolean
)

// Single message bubble
@Composable
fun MessageBubble(message: Message) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = if (message.isSender) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            color = if (message.isSender) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(12.dp),
            shadowElevation = 2.dp
        ) {
            Text(
                text = message.text,
                color = if (message.isSender) Color.White else Color.Black,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}

// Messages list
@Composable
fun MessagesList(messages: List<Message>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(8.dp)
    ) {
        messages.forEach { msg ->
            MessageBubble(msg)
        }
    }
}

// Input field with send button
@Composable
fun MessageInput(onSend: (String) -> Unit) {
    var messageText by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = messageText,
            onValueChange = { messageText = it },
            modifier = Modifier.weight(1f),
            placeholder = { Text("Type a message") }
        )

        Spacer(modifier = Modifier.width(8.dp))

        Button(onClick = {
            if (messageText.isNotBlank()) {
                onSend(messageText)
                messageText = ""
            }
        }) {
            Text("Send")
        }
    }
}

// Complete Chat Screen UI
@Composable
fun ChatScreenUI() {
    val messages = remember {
        mutableStateListOf(
            Message("Hello!", false),
            Message("Hi, how are you?", true),
            Message("I am fine, thanks!", false)
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        MessagesList(messages = messages)
        Divider()
        MessageInput { newMessage ->
            messages.add(Message(newMessage, true))
        }
    }
}
