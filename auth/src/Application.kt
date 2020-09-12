package com.example

import com.example.main.kotlin.br.com.authKtor.configuration.DataSource
import com.example.main.kotlin.br.com.authKtor.configuration.RedisConfiguration
import com.example.main.kotlin.br.com.authKtor.controller.authController
import com.example.main.kotlin.br.com.authKtor.controller.userController
import com.example.main.kotlin.br.com.authKtor.exception.NotFoundException
import com.example.main.kotlin.br.com.authKtor.extension.AUTHENTICATION_PROVIDER
import com.example.main.kotlin.br.com.authKtor.model.UserCredential
import com.example.main.kotlin.br.com.authKtor.service.AuthService
import com.google.gson.*
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.sessions.*
import io.ktor.util.*
import java.lang.reflect.Type

class ParametersSerializer : JsonDeserializer<Any>, JsonSerializer<Any> {

    @InternalAPI
    override fun deserialize(
        jsonElement: JsonElement,
        type: Type,
        jsonDeserializationContext: JsonDeserializationContext
    ): Any =
        jsonDeserializationContext.deserialize(jsonElement.asJsonObject, ParametersImpl::class.java)

    override fun serialize(
        jsonElement: Any,
        type: Type,
        jsonSerializationContext: JsonSerializationContext
    ): JsonElement =
        JsonObject()
}

fun main(args: Array<String>) {
    val server = embeddedServer(Netty, 8080) {
        DataSource()
        RedisConfiguration.connect()
        install(Authentication) {
            form(AUTHENTICATION_PROVIDER) {
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
                setPrettyPrinting()
                registerTypeAdapter(Parameters::class.java, ParametersSerializer())
            }
        }

        install(Sessions) {
            cookie<UserCredential>("authentication-session", SessionStorageMemory()) {
                cookie.path = "/" // Specify cookie's path '/' so it can be used in the whole site
            }
        }

        routing {
            userController()
            authController()
        }
    }
    server.start(wait = true)
}