package net.rsandor.plaything.user.services

import net.rsandor.plaything.user.model.User
import net.rsandor.plaything.user.utils.Try
import net.rsandor.plaything.user.web.UserRequest
import java.util.*

interface UserService {

    fun getAll(): Collection<User>

    fun getById(id: UUID): User?

    fun create(request: UserRequest): Try<User>

    fun update(id: UUID, request: UserRequest): Try<User?>

    fun partialUpdate(id: UUID, request: UserRequest): User?

    fun delete(id: UUID)

}

