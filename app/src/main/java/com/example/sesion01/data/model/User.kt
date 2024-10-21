package com.example.sesion01.data.model

data class User(
    val id: Long = 0L,
    val name: String,
    val email: String = "No email",
    val phone: String = "No phone"
)
