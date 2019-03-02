package net.rsandor.plaything.user.web.utils

import io.ktor.http.HttpStatusCode

data class HttpResponse<T>(
    val statusCode: Int,
    val message: String?,
    val messages: List<String>?,
    val response: T?
) {
    constructor(statusCode: Int, message: String, response: T?) : this(statusCode, message, null, response)

    constructor(statusCode: Int, messages: List<String>, response: T?) : this(statusCode, null, messages, response)

    companion object {
        fun <T> ok(response: T): HttpResponse<T> = HttpResponse(HttpStatusCode.OK.value, "success", response)

        fun <T> created(response: T?): HttpResponse<T> = HttpResponse(HttpStatusCode.Created.value, "created", response)

        fun notFound(): HttpResponse<Any> = HttpResponse(HttpStatusCode.NotFound.value, "not found", null)

        fun badRequest(message: String): HttpResponse<Any> = badRequest(listOf(message))

        fun badRequest(messages: List<String>): HttpResponse<Any> =
            HttpResponse(HttpStatusCode.BadRequest.value, messages, null)
    }
}

