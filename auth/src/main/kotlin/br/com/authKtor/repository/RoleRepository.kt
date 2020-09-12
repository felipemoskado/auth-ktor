package com.example.main.kotlin.br.com.authKtor.repository

import org.jooq.Tables.ROLE

object RoleRepository : CRUDRepository<Long>(ROLE, ROLE.ID) {

    fun findIdByRole(roleName: String): Long? =
        query { select(ROLE.ID).from(ROLE).where(ROLE.ROLE_.eq(roleName)).fetchAnyInto(Long::class.java) }
}