package com.example

import com.example.main.kotlin.br.com.authKtor.configuration.DataSource
import com.example.main.kotlin.br.com.authKtor.configuration.RedisConfiguration
import com.example.main.kotlin.br.com.authKtor.exception.NotFoundException
import com.example.main.kotlin.br.com.authKtor.model.UserCredential
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
import io.ktor.sessions.*

fun main(args: Array<String>) {
    val server = embeddedServer(Netty, 8080) {
        DataSource()
        RedisConfiguration.connect()
        install(Authentication) {
            form("login") {
                userParamName = "username"
                skipWhen { call -> call.sessions.get<UserCredential>() != null }

                validate { credentails ->
                    try {
                        AuthService.authenticate(credentails.name, credentails.password)
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

        install(Sessions) {
            cookie<UserCredential>("authentication-session", SessionStorageMemory()) {
                cookie.path = "/" // Specify cookie's path '/' so it can be used in the whole site
            }
        }

        routing {
            authenticate("login") {
                get("/") {
                    call.respondText("Hello, world!", ContentType.Text.Html)
                }
            }

            route("/login") {
                authenticate("login") {
                    post {
                        call.sessions.set(call.principal<UserCredential>())
                        call.respondRedirect("/", permanent = false)
                    }
                }
            }
        }
    }
    server.start(wait = true)
}