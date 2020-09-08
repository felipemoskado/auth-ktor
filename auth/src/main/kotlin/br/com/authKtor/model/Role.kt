package com.example.main.kotlin.br.com.authKtor.model

data class Role(val id: Long, val type: RoleType)

enum class RoleType(val level: Int) {
    OPERATOR(10), MANAGER(20), ADMIN(50), SUPER_ADMIN(100)
}