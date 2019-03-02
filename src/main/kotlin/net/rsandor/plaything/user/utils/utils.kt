package net.rsandor.plaything.user.utils

sealed class Try<T> {
    companion object {
        fun <T> to(action: () -> T): Try<T> = try {
            Success(action())
        } catch (ex: Exception) {
            Failure(ex)
        }
    }

    data class Success<T>(val value: T) : Try<T>()
    class Failure<T>(val exception: Exception) : Try<T>()
}

sealed class Either<out A, out B> {
    class Left<A>(val value: A) : Either<A, Nothing>()
    class Right<B>(val value: B) : Either<Nothing, B>()
}