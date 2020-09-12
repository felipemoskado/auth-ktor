package com.example.main.kotlin.br.com.authKtor.repository

import com.example.main.kotlin.br.com.authKtor.configuration.DataSource
import org.jooq.*
import org.jooq.impl.DSL
import org.jooq.impl.TableImpl

open class CRUDRepository<ID>(val table: TableImpl<*>, val tableId: TableField<out Record, ID>) : DataSource() {

    fun <T> query(block: DSLContext.() -> T): T = block(getDSLContext())

    inline fun <reified RECORD : Record> insert(record: RECORD) =
        query { insertInto(table, *record.fields()).values(*record.valuesRow().fields()).execute() }

    inline fun <reified RECORD> findById(id: ID): RECORD? =
        query { selectFrom(table).where(tableId.eq(id)).fetchSingleInto(RECORD::class.java) }

    inline fun <reified RECORD : Record> findAll(): List<RECORD> =
        query { selectFrom(table).fetchInto(RECORD::class.java) }

    private fun getDSLContext(): DSLContext = DSL.using(hikariDataSource, SQLDialect.POSTGRES)
}