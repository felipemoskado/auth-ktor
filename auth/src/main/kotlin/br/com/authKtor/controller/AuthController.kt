package com.example.main.kotlin.br.com.authKtor.controller

import com.example.main.kotlin.br.com.authKtor.model.UserCredential
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.request.*
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

suspend fun PipelineContext<Unit, ApplicationCall>.loginAction() {
    call.sessions.set(call.principal<UserCredential>())
    call.respond("Authenticated.")
}