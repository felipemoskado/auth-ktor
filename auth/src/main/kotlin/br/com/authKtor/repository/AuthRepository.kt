package com.example.main.kotlin.br.com.authKtor.repository

import org.jooq.Record
import org.jooq.Tables
import org.jooq.impl.DSL

object AuthRepository : CRUDRepository<String>(Tables.LOGIN, Tables.LOGIN.EMAIL) {

    fun authenticate(email: String, password: String): Record? =
        query {
            select(emptyList())
                .from(Tables.LOGIN)
                .join(Tables.ROLE).on(Tables.ROLE.ID.eq(Tables.LOGIN.ROLE_ID))
                .where(
                    DSL.and(
                        Tables.LOGIN.EMAIL.eq(email),
                        Tables.LOGIN.SECRET_PASSWORD.eq(password),
                        Tables.LOGIN.ACTIVE.eq(true)
                    )
                )
                .fetchOne()
        }
}