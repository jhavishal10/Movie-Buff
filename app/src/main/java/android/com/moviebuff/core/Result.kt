package android.com.moviebuff.core

sealed class Result<out T : Any> {
    data class Success<out T : Any>(val data: T) : Result<T>()
    object SuccessWithNoContent : Result<Nothing>()
    data class Error(val error: ErrorBody) : Result<Nothing>()
}