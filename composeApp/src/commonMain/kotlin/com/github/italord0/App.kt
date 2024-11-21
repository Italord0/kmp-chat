package com.github.italord0

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.italord0.data.Message
import com.github.italord0.data.MessageRepository
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun App() {
    val messageRepository = remember { MessageRepository() }
    val messages by messageRepository.getMessages().collectAsState(listOf())
    var messageText by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    var isSending by remember { mutableStateOf(false) }
    val lazyListState = rememberLazyListState()
    var userName by remember { mutableStateOf("") }
    var userNameTextField by remember { mutableStateOf("") }

    if (userName.isEmpty()) {
        Column(Modifier.fillMaxSize().padding(16.dp)) {
            Text(modifier = Modifier.fillMaxWidth(), text = "Enter Name :", textAlign = TextAlign.Center)
            TextField(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                value = userNameTextField,
                onValueChange = { userNameTextField = it },
                placeholder = { Text("Enter your name") },
            )
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    userName = userNameTextField
                }
            ) {
                Text("Enter Chat")
            }
        }
    } else {

        LaunchedEffect(messages.size) {
            if (messages.isNotEmpty()) {
                lazyListState.animateScrollToItem(messages.lastIndex)
            }
        }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            // LazyColumn for messages with scrollability and spacing between items
            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .weight(1f)  // Takes up remaining space above the input
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(messages) { message ->
                    MessageBubble(Modifier.animateItemPlacement(), message)
                }
            }

            // Input Field and Send Button, fixed to the bottom
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    placeholder = { Text("Enter your message") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )

                Button(
                    onClick = {
                        isSending = true
                        coroutineScope.launch {
                            messageRepository.addMessage(
                                Message(
                                    author = userName,
                                    content = messageText,
                                    createdAt = Clock.System.now().epochSeconds,
                                    platform = getPlatform().name
                                )
                            )
                            messageText = ""
                        }
                        isSending = false
                    },
                    enabled = !isSending && messageText.isNotBlank(),
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Text("Send")
                }
            }
        }
    }
}

@Composable
fun MessageBubble(modifier: Modifier = Modifier, message: Message) {
    Card(
        modifier = modifier.fillMaxWidth().padding(8.dp),
        backgroundColor = Color.LightGray,
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(text = "${message.author} :", style = TextStyle(fontSize = 16.sp))
            Text(text = message.content, style = TextStyle(fontSize = 14.sp))
            Spacer(Modifier.height(12.dp))
            Row {
                Text(
                    text = Instant.fromEpochSeconds(message.createdAt).toString(),
                    style = TextStyle(fontSize = 8.sp)
                )
                Spacer(Modifier.width(24.dp))
                Text(text = message.platform, style = TextStyle(fontSize = 8.sp))
            }
        }
    }
}

@Composable
@Preview
fun MessageBubblePreview() {
    MessageBubble(
        message = Message(
            id = "",
            author = "Preview",
            content = "This is a serious message",
            createdAt = 1231231,
            platform = "IDE"
        )
    )
}