package com.example.main.kotlin.br.com.authKtor.model

import io.ktor.auth.*

data class UserCredential(val username: String, val password: String, val role: RoleType) : Principal