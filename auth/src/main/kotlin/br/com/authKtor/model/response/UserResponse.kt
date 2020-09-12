package com.example.main.kotlin.br.com.authKtor.model.response

import com.example.main.kotlin.br.com.authKtor.model.RoleType

data class UserResponse(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val role: RoleType
)