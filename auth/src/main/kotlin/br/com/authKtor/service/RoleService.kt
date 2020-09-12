package com.example.main.kotlin.br.com.authKtor.service

import com.example.main.kotlin.br.com.authKtor.exception.NotFoundException
import com.example.main.kotlin.br.com.authKtor.model.RoleType
import com.example.main.kotlin.br.com.authKtor.model.entry.RoleEntry
import com.example.main.kotlin.br.com.authKtor.model.response.RoleResponse
import com.example.main.kotlin.br.com.authKtor.repository.RoleRepository
import org.jooq.tables.records.RoleRecord

object RoleService {

    fun create(roleEntry: RoleEntry) =
        RoleRepository.create(toRecord(roleEntry))

    fun findAll(): List<RoleResponse> = RoleRepository.findAll<RoleRecord>()
        .asSequence()
        .map { RoleResponse(it.id, RoleType.valueOf(it.role)) }
        .toList()

    fun findIdByRole(role: RoleType) =
        RoleRepository.findIdByRole(role.name) ?: throw NotFoundException("Role not found.")

    private fun toRecord(entry: RoleEntry): RoleRecord = RoleRecord().apply {
        level = entry.name.level
        role = entry.name.name
    }
}