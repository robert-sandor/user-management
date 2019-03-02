package net.rsandor.plaything.user.services.impl

import com.google.inject.Inject
import net.rsandor.plaything.user.model.User
import net.rsandor.plaything.user.repo.UserDao
import net.rsandor.plaything.user.services.UserService
import net.rsandor.plaything.user.utils.Failure
import net.rsandor.plaything.user.utils.Success
import net.rsandor.plaything.user.utils.Try
import net.rsandor.plaything.user.web.UserRequest
import java.util.*

class UserServiceImpl @Inject constructor(
    private val userDao: UserDao,
    private val userValidator: UserValidator
) : UserService {

    override fun partialUpdate(id: UUID, request: UserRequest): User? {
        val user = getById(id)
        return user?.let { userDao.update(id, it.partialUpdate(request)) }
    }

    override fun getAll(): Collection<User> = userDao.getAll()

    override fun getById(id: UUID): User? = userDao.getById(id)

    override fun create(request: UserRequest): Try<User> =
        validateRequestAndThen(request) {
            val newUser = User(UUID.randomUUID(), request)
            userDao.create(newUser)
            newUser
        }

    override fun update(id: UUID, request: UserRequest): Try<User?> =
        validateRequestAndThen(request) {
            val user = User(id, it)
            val existingUser = userDao.update(id, user)
            when (existingUser) {
                null -> user
                else -> null
            }
        }

    override fun delete(id: UUID) = userDao.delete(id)

    private fun <T> validateRequestAndThen(userRequest: UserRequest, transform: (UserRequest) -> T): Try<T> {
        val errors = userValidator.validate(userRequest)
        return when (errors.count()) {
            0 -> Success(transform(userRequest))
            else -> Failure(IllegalArgumentException(errors.joinToString()))
        }
    }
}