package com.example.main.kotlin.br.com.authKtor.extension

import com.example.main.kotlin.br.com.authKtor.exception.AuthenticationException
import com.example.main.kotlin.br.com.authKtor.exception.UnauthorizedException
import com.example.main.kotlin.br.com.authKtor.model.RoleType
import com.example.main.kotlin.br.com.authKtor.model.UserCredential
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.util.pipeline.*

const val AUTHENTICATION_PROVIDER = "login"

fun Routing.roleAllowed(role: RoleType, route: Route.() -> Unit) {
    authenticate(AUTHENTICATION_PROVIDER) {
        checkRole(role)
        route(this)
    }
}

private fun Route.checkRole(role: RoleType) {
    val checkRole = PipelinePhase("checkRole")
    insertPhaseBefore(ApplicationCallPipeline.Features, checkRole)
    intercept(checkRole) {
        val user = call.sessions.get<UserCredential>()

        try {
            user.optional(
                caseNone = { throw AuthenticationException("Required authentication.") },
                caseSome = {
                    if (it.role.level < role.level)
                        throw UnauthorizedException("Permission Denied.")
                }
            )
        } catch (ex: AuthenticationException) {
            call.respond(HttpStatusCode.Unauthorized, ex.message)
        } catch (ex: UnauthorizedException) {
            call.respond(HttpStatusCode.Forbidden, ex.message)
        }
    }
}