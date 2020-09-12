package com.example.main.kotlin.br.com.authKtor.repository

import org.jooq.Record5
import org.jooq.Result
import org.jooq.Tables.LOGIN
import org.jooq.Tables.ROLE

object UserRepository : CRUDRepository<String>(LOGIN, LOGIN.EMAIL) {

    fun listAll(): Result<Record5<String, String, String, String, String>> = query {
        select(LOGIN.EMAIL, LOGIN.SECRET_PASSWORD, LOGIN.FIRST_NAME, LOGIN.LAST_NAME, ROLE.ROLE_)
            .from(LOGIN)
            .join(ROLE).on(ROLE.ID.eq(LOGIN.ROLE_ID))
            .fetch()
    }
}