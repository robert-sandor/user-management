package net.rsandor.plaything.user.repo

import net.rsandor.plaything.user.model.User
import java.util.*

interface UserDao {

    fun getAll(): Collection<User>

    fun getById(id: UUID): User?

    fun create(user: User)

    fun update(id: UUID, user: User): User?

    fun delete(id: UUID)

}
