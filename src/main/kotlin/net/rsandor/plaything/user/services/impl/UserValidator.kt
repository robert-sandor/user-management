package net.rsandor.plaything.user.services.impl

import net.rsandor.plaything.user.web.UserRequest
import javax.mail.internet.AddressException
import javax.mail.internet.InternetAddress

class UserValidator {

    fun validate(request: UserRequest): Iterable<String> {
        val errors = mutableListOf<String>()

        request.firstName ?: errors.add("User first name cannot be null!")

        request.lastName ?: errors.add("User last name cannot be null!")

        request.role ?: errors.add("User role cannot be null!")

        request.email?.let { getValidEmailAddress(it) }
            ?: errors.add("Value [${request.email}] for email address is not valid!")

        return errors
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
