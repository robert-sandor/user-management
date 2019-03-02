package net.rsandor.plaything.user.web

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import net.rsandor.plaything.user.services.UserService
import net.rsandor.plaything.user.utils.Try
import net.rsandor.plaything.user.utils.UUIDs
import net.rsandor.plaything.user.web.utils.HttpResponse

class UserRouter @Inject constructor(application: Application, userService: UserService) {
    init {
        application.routing {
            route("api") {
                route("users") {
                    get {
                        call.respond(HttpStatusCode.OK, HttpResponse.ok(userService.getAll()))
                    }

                    post {
                        val user: UserRequest = call.receive()
                        val response = userService.create(user)

                        when (response) {
                            is Try.Success -> call.respond(HttpStatusCode.Created, HttpResponse.created(response.value))
                            is Try.Failure -> call.respond(
                                HttpStatusCode.BadRequest,
                                HttpResponse.badRequest(response.exception.message ?: "Unknown error.")
                            )
                        }

                    }

                    route("{userId}") {
                        get {
                            val userId = UUIDs.decode(call.parameters["userId"])
                            val user = userId?.let { userService.getById(it) }

                            when (user) {
                                null -> call.respond(HttpStatusCode.NotFound, HttpResponse.notFound())
                                else -> call.respond(HttpStatusCode.OK, HttpResponse.ok(user))
                            }
                        }

                        put {
                            val userId = UUIDs.decode(call.parameters["userId"])
                            val request: UserRequest = call.receive()

                            val update = userId?.let { userService.update(it, request) }
                            when (update) {
                                null -> call.respond(HttpStatusCode.NotFound, HttpResponse.notFound())
                                is Try.Success -> {
                                    when (update.value) {
                                        null -> call.respond(HttpStatusCode.NoContent)
                                        else -> call.respond(
                                            HttpStatusCode.Created,
                                            HttpResponse.created(update.value)
                                        )
                                    }
                                }
                                is Try.Failure -> {
                                    call.respond(
                                        HttpStatusCode.BadRequest,
                                        HttpResponse.badRequest(update.exception.message ?: "Unknown error.")
                                    )
                                }
                            }
                        }

                        patch {
                            val userId = UUIDs.decode(call.parameters["userId"])
                            val request: UserRequest = call.receive()

                            val update = userId?.let { userService.partialUpdate(it, request) }
                            when (update) {
                                null -> call.respond(HttpStatusCode.NotFound, HttpResponse.notFound())
                                else -> call.respond(HttpStatusCode.NoContent)
                            }
                        }

                        delete {
                            UUIDs.decode(call.parameters["userId"])?.let { userService.delete(it) }
                            call.respond(HttpStatusCode.NoContent)
                        }
                    }
                }
            }
        }
    }
}