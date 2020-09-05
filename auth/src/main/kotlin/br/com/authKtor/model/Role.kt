package com.example.main.kotlin.br.com.authKtor.model

data class Role(val id: Long, val type: RoleType)

enum class RoleType {
    OPERATOR, MANAGER, ADMIN
}