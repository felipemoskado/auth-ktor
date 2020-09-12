package com.example.main.kotlin.br.com.authKtor.controller

import com.example.main.kotlin.br.com.authKtor.extension.roleAllowed
import com.example.main.kotlin.br.com.authKtor.model.RoleType
import com.example.main.kotlin.br.com.authKtor.model.entry.UserEntry
import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*

fun Route.userController() {
    route("/users") {
        roleAllowed(RoleType.SUPER_ADMIN) {
            post { createAction() }
        }
    }
}

suspend fun PipelineContext<Unit, ApplicationCall>.createAction() {
    receiveModel<UserEntry>()
    println("return")
}