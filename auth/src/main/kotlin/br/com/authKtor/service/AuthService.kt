package com.example.main.kotlin.br.com.authKtor.service

import com.example.main.kotlin.br.com.authKtor.exception.NotFoundException
import com.example.main.kotlin.br.com.authKtor.model.RoleType
import com.example.main.kotlin.br.com.authKtor.model.UserCredential
import com.example.main.kotlin.br.com.authKtor.repository.AuthRepository
import io.ktor.auth.*
import org.jooq.Record
import org.jooq.Tables

object AuthService {

    fun authenticate(email: String, password: String): Principal = run {
        val record = AuthRepository.authenticate(email, password) ?: throw NotFoundException("User not found")

        convertRecordToModel(record)
    }

    private fun convertRecordToModel(record: Record): Principal =
        UserCredential(
            username = record[Tables.LOGIN.EMAIL],
            password = record[Tables.LOGIN.SECRET_PASSWORD],
            role = RoleType.valueOf(record[Tables.ROLE.ROLE_])
        )
}