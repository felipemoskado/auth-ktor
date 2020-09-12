package com.example.main.kotlin.br.com.authKtor.service

import com.example.main.kotlin.br.com.authKtor.model.RoleType
import com.example.main.kotlin.br.com.authKtor.model.entry.UserEntry
import com.example.main.kotlin.br.com.authKtor.model.response.UserResponse
import com.example.main.kotlin.br.com.authKtor.repository.UserRepository
import org.jooq.Record
import org.jooq.Tables
import org.jooq.Tables.*
import org.jooq.tables.records.LoginRecord

object UserService {

    fun create(entry: UserEntry) {
        val roleId = RoleService.findIdByRole(entry.role)
        UserRepository.insert(toRecord(entry, roleId))
    }

    fun findAll(): List<UserResponse> =
        UserRepository.listAll().asSequence().map { toModel(it) }.toList()

    private fun toModel(record: Record): UserResponse = UserResponse(
        email = record[LOGIN.EMAIL],
        password = record[LOGIN.SECRET_PASSWORD],
        firstName = record[LOGIN.FIRST_NAME],
        lastName = record[LOGIN.LAST_NAME],
        role = RoleType.valueOf(record[ROLE.ROLE_])
    )

    private fun toRecord(entry: UserEntry, roleId: Long): LoginRecord = LoginRecord().apply {
        email = entry.email
        secretPassword = entry.password
        firstName = entry.firstName
        lastName = entry.lastName
        this.roleId = roleId
        active = true
    }
}