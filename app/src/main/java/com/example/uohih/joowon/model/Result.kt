package com.example.uohih.joowon.model

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out T : Any> {

    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Failure<out T : Any>(val data: T) : Result<T>()
    data class Error(val exception: Any) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Failure<*> -> "Failure[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}
