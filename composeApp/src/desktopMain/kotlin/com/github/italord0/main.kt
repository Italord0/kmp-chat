package com.github.italord0

import android.app.Application
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.google.firebase.FirebasePlatform
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseOptions
import dev.gitlive.firebase.initialize

fun main() = application {

    FirebasePlatform.initializeFirebasePlatform(object : FirebasePlatform() {

        val storage = mutableMapOf<String, String>()
        override fun clear(key: String) {
            storage.remove(key)
        }

        override fun log(msg: String) = println(msg)

        override fun retrieve(key: String) = storage[key]

        override fun store(key: String, value: String) = storage.set(key, value)

    })

    val options = FirebaseOptions(
        projectId = "kmp-chat-cfbdf",
        applicationId = "1:684096295074:web:abde6fe3909bda5041c7fb",
        apiKey = "AIzaSyDNzxW0roP83Bf3c_kLWP2C_zRE4VRsT3E"
    )

    Firebase.initialize(Application(), options)

    Window(
        onCloseRequest = ::exitApplication,
        title = "kmp-chat",
    ) {
        App()
    }
}