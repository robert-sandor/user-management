package net.rsandor.plaything.user.utils

sealed class Try<T>
data class Success<T>(val result: T) : Try<T>()
data class Failure<T>(val exceptions: List<Exception>) : Try<T>() {
    constructor(exception: Exception): this(listOf(exception))
}

