package com.github.italord0

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform