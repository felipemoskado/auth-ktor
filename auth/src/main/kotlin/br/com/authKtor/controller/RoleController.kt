package com.example.main.kotlin.br.com.authKtor.controller

import com.example.main.kotlin.br.com.authKtor.extension.receiveBodyModel
import com.example.main.kotlin.br.com.authKtor.extension.roleAllowed
import com.example.main.kotlin.br.com.authKtor.model.RoleType
import com.example.main.kotlin.br.com.authKtor.model.entry.RoleEntry
import com.example.main.kotlin.br.com.authKtor.service.RoleService
import com.example.main.kotlin.br.com.authKtor.utils.toJson
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*

fun Route.roleController() {
    route("/roles") {
        roleAllowed(RoleType.ADMIN) {
            post { createRoleAction() }
            get { listRoleAction() }
        }
    }
}

suspend fun PipelineContext<Unit, ApplicationCall>.createRoleAction() {
    receiveBodyModel<RoleEntry> { RoleService.create(it) }
}

suspend fun PipelineContext<Unit, ApplicationCall>.listRoleAction() {
    call.respond(HttpStatusCode.OK, RoleService.findAll().toJson())
}