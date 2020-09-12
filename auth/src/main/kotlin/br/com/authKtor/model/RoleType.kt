package com.example.main.kotlin.br.com.authKtor.model

enum class RoleType(val level: Int) {
    OPERATOR(10), MANAGER(20), ADMIN(50), SUPER_ADMIN(100)
}