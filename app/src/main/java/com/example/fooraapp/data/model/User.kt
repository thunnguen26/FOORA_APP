package com.example.fooraapp.data.model

data class User(
    val uid: String = "",
    val displayname: String = "",
    val email: String = "",
    val phone: String = "",
    val photoUrl: String = "",
    val role: String = "user",
    val isActivived: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)