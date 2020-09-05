package com.example

import com.example.main.kotlin.br.com.authKtor.configuration.DataSource
import com.example.main.kotlin.br.com.authKtor.configuration.RedisConfiguration
import com.example.main.kotlin.br.com.authKtor.exception.NotFoundException
import com.example.main.kotlin.br.com.authKtor.service.AuthService
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    val server = embeddedServer(Netty, 8080) {
        DataSource()
        RedisConfiguration.connect()
        install(Authentication) {
            form("login") {
                userParamName = "username"

                validate { credentails ->
                    try {
                        AuthService.authenticate(credentails.name, credentails.password)
                        UserIdPrincipal(credentails.name)
                    } catch (ex: NotFoundException) {
                        null
                    }
                }
            }
        }

        install(DefaultHeaders)
        install(CallLogging)
        install(ContentNegotiation) {
            gson {
                serializeNulls()
            }
        }

        routing {
            get("/") {
                call.respondText("Hello, world!", ContentType.Text.Html)
            }

            route("/login") {
                authenticate("login") {
                    post {
                        call.respondRedirect("/", permanent = false)
                    }
                }
            }
        }
    }
    server.start(wait = true)
}