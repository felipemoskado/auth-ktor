package com.example.main.kotlin.br.com.authKtor.configuration

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.io.FileInputStream
import java.util.*

open class DataSource {
    private val dir = "./auth/resources/application.properties"
    val hikariDataSource: HikariDataSource

    init {
        val properties = Properties().apply { load(FileInputStream(dir)) }
        val host = properties.getProperty("dataSource.host")
        val port = properties.getProperty("dataSource.port")
        val database = properties.getProperty("dataSource.database")
        val username = properties.getProperty("dataSource.username")
        val password = properties.getProperty("dataSource.password")

        val hikariConfig = HikariConfig().apply {
            jdbcUrl = "jdbc:postgresql://$host:$port/$database"
            this.username = username
            this.password = password
        }

        hikariDataSource = HikariDataSource(hikariConfig)
    }

}