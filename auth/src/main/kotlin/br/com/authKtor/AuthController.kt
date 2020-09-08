package com.example.main.kotlin.br.com.authKtor

import com.example.main.kotlin.br.com.authKtor.model.UserCredential
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.util.pipeline.*

fun Route.authController() {
    route("/login") {
        authenticate("login") {
            post { loginAction() }
        }
    }
}

suspend fun PipelineContext<Unit, ApplicationCall>.loginAction() = run {
    call.sessions.set(call.principal<UserCredential>())
    call.respond("Authenticated.")
}
