package net.rsandor.plaything.user.repo.impl

import net.rsandor.plaything.user.model.User
import net.rsandor.plaything.user.repo.UserDao
import java.util.*
import kotlin.collections.HashMap

class UserDaoImpl : UserDao {
    private val users: MutableMap<UUID, User> = HashMap()

    override fun getAll(): Collection<User> = users.values

    override fun getById(id: UUID): User? = users[id]

    override fun create(user: User) {
        users[user.id] = user
    }

    override fun update(id: UUID, user: User): User? {
        return users.put(id, user)
    }

    override fun delete(id: UUID) {
        users.remove(id)
    }

}
