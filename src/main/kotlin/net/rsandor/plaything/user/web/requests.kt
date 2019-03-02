package net.rsandor.plaything.user.web

import net.rsandor.plaything.user.model.UserRole

data class UserRequest(
    val firstName: String?,
    val lastName: String?,
    val role: UserRole?,
    val email: String?
)