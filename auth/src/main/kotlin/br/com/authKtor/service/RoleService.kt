package com.example.main.kotlin.br.com.authKtor.service

import com.example.main.kotlin.br.com.authKtor.exception.NotFoundException
import com.example.main.kotlin.br.com.authKtor.model.RoleType
import com.example.main.kotlin.br.com.authKtor.repository.RoleRepository

object RoleService {

    fun findIdByRole(role: RoleType) =
        RoleRepository.findIdByRole(role.name) ?: throw NotFoundException("Role not found.")
}