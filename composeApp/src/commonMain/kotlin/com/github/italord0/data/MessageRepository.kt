package com.github.italord0.data

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.flow

class MessageRepository {
    private val firestoreCollection = Firebase.firestore.collection("messages")

    fun getMessages() = flow {
        firestoreCollection.snapshots.collect { querySnapshot ->
            val messages = querySnapshot.documents.map { documentSnapshot ->
                with(documentSnapshot.data<Message>()) {
                    Message(
                        id = documentSnapshot.id,
                        author = author,
                        createdAt = createdAt,
                        content = content,
                        platform = platform
                    )
                }
            }
            emit(messages.sortedBy { it.createdAt })
        }
    }

    suspend fun addMessage(message: Message) {
        val messageId = generateRandomStringId()
        firestoreCollection
            .document(messageId)
            .set(message.copy(id = messageId))
    }

    private fun generateRandomStringId(length: Int = 20): String {
        val allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }
}