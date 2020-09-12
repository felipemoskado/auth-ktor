package com.example.main.kotlin.br.com.authKtor.repository

import org.jooq.Tables.ROLE
import org.jooq.impl.DSL
import org.jooq.tables.records.RoleRecord

object RoleRepository : CRUDRepository<Long>(ROLE, ROLE.ID) {

    fun create(record: RoleRecord) {
        query { insertInto(ROLE, ROLE.ROLE_, ROLE.LEVEL).values(record.role, record.level).execute() }
    }

    fun findIdByRole(roleName: String): Long? =
        query { select(ROLE.ID).from(ROLE).where(ROLE.ROLE_.eq(roleName)).fetchAnyInto(Long::class.java) }
}