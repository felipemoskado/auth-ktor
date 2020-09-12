package com.example.main.kotlin.br.com.authKtor.controller

import com.example.main.kotlin.br.com.authKtor.extension.receiveBodyModel
import com.example.main.kotlin.br.com.authKtor.extension.roleAllowed
import com.example.main.kotlin.br.com.authKtor.model.RoleType
import com.example.main.kotlin.br.com.authKtor.model.entry.UserEntry
import com.example.main.kotlin.br.com.authKtor.service.UserService
import com.example.main.kotlin.br.com.authKtor.utils.toJson
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*

fun Route.userController() {
    route("/users") {
        roleAllowed(RoleType.ADMIN) {
            post { createAction() }
            get { listAction() }
        }
    }
}

suspend fun PipelineContext<Unit, ApplicationCall>.createAction() {
    receiveBodyModel<UserEntry> { UserService.create(it) }
}

suspend fun PipelineContext<Unit, ApplicationCall>.listAction() {
    call.respond(HttpStatusCode.OK, UserService.findAll().toJson())
}