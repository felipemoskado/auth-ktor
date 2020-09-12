package com.example.main.kotlin.br.com.authKtor.extension

import com.example.main.kotlin.br.com.authKtor.exception.PermissionDeniedException
import com.example.main.kotlin.br.com.authKtor.exception.UnauthorizedException
import com.example.main.kotlin.br.com.authKtor.model.RoleType
import com.example.main.kotlin.br.com.authKtor.model.UserCredential
import com.example.main.kotlin.br.com.authKtor.model.entry.EntryModel
import com.example.main.kotlin.br.com.authKtor.utils.toJson
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.util.pipeline.*

const val AUTHENTICATION_PROVIDER = "login"

fun Route.roleAllowed(role: RoleType, route: Route.() -> Unit) {
    authenticate(AUTHENTICATION_PROVIDER) {
        checkRole(role)
        route(this)
    }
}

private fun Route.checkRole(role: RoleType) {
    val checkRole = PipelinePhase("checkRole")
    insertPhaseBefore(ApplicationCallPipeline.Call, checkRole)
    intercept(checkRole) {
        val user = call.sessions.get<UserCredential>()

        try {
            user.optional(
                caseSome = {
                    if (it.role.level < role.level) {
                        println("Permission Denied.")
                        throw PermissionDeniedException("Permission Denied.")
                    }
                },
                caseNone = {
                    println("Required authentication.")
                    throw UnauthorizedException("Required authentication.")
                }
            )
        } catch (ex: PermissionDeniedException) {
            finish()
            call.respond(HttpStatusCode.Unauthorized, ex.message)
        } catch (ex: UnauthorizedException) {
            finish()
            call.respond(HttpStatusCode.Forbidden, ex.message)
        }
    }
}

suspend inline fun <reified ENTRY : EntryModel> PipelineContext<Unit, ApplicationCall>.receiveBodyModel(block: (ENTRY) -> Any?) {
    try {
        val payload = call.receive<ENTRY>()
        block(payload)
    } catch (t: Throwable) {
        t
    }.let { result ->
        when (result) {
            null, Unit ->
                call.respond(HttpStatusCode.Created, "OK")
            is UnauthorizedException ->
                call.respond(HttpStatusCode.Unauthorized, result.message)
            else ->
                call.respond(HttpStatusCode.OK, result.toJson())
        }
    }
}
