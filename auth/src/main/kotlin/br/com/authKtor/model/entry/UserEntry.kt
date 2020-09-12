package com.example.main.kotlin.br.com.authKtor.model.entry

import com.example.main.kotlin.br.com.authKtor.model.RoleType

data class UserEntry(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val role: RoleType
)