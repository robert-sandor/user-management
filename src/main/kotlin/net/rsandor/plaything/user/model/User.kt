package net.rsandor.plaything.user.model

import net.rsandor.plaything.user.web.UserRequest
import java.util.*

data class User(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val role: UserRole,
    val email: String
) {
    constructor(id: UUID, user: UserRequest) : this(id, user.firstName!!, user.lastName!!, user.role!!, user.email!!)

    fun partialUpdate(request: UserRequest): User {
        return User(
            id,
            request.firstName ?: firstName,
            request.lastName ?: lastName,
            request.role ?: role,
            request.email ?: email
        )
    }
}

enum class UserRole {
    USER,
    ADMIN
}
