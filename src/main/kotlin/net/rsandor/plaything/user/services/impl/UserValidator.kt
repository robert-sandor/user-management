package net.rsandor.plaything.user.services.impl

import net.rsandor.plaything.user.utils.Either
import net.rsandor.plaything.user.web.UserRequest
import javax.mail.internet.AddressException
import javax.mail.internet.InternetAddress

class UserValidator {

    /**
     * Validates a UserRequest object
     *
     * @param request The UserRequest object to validate
     *
     * @return Either an Iterable of error messages, or a UserRequest object that has been validated
     */
    fun validate(request: UserRequest): Either<Iterable<String>, UserRequest> {
        val errors = mutableListOf<String>()

        request.firstName ?: errors.add("User first name cannot be null!")

        request.lastName ?: errors.add("User last name cannot be null!")

        request.role ?: errors.add("User role cannot be null!")

        val validEmail = request.email?.let { getValidEmailAddress(it) }

        validEmail ?: errors.add("Value [${request.email}] for email address is not valid!")

        return if (errors.isEmpty()) Either.Right(request.copy(email = validEmail)) else Either.Left(errors)
    }

    private fun getValidEmailAddress(string: String): String? {
        return try {
            val trimmedString = string.trim()
            InternetAddress(trimmedString, true)
            trimmedString
        } catch (addressException: AddressException) {
            null
        }
    }

}

