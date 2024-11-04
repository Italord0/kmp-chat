package com.github.italord0.data

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val id : String = "",
    val author: String,
    val content: String,
    val createdAt: Long,
    val platform: String
)