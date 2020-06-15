package android.com.moviebuff.core

import android.com.moviebuff.core.Result.Error
import com.google.gson.Gson
import retrofit2.Response
import java.net.HttpURLConnection

object RemoteSource {

    private const val DEFAULT_RETRY_COUNT = 3

    suspend fun <T : Any> safeApiCall(
        call: suspend () -> Response<T>
    ): Result<T> {
        var count = 0
        while (count < DEFAULT_RETRY_COUNT) {
            try {
                val response = call.invoke()
                count = DEFAULT_RETRY_COUNT + 1
                if (response.isSuccessful) {
                    return if (response.body() != null) {
                        Result.Success(response.body()!!)
                    } else {
                        Result.SuccessWithNoContent
                    }
                }
                return handleResponseFailure(
                    response
                )
            } catch (exception: Exception) {
                exception.printStackTrace()
                return Error(
                    ErrorBody(
                        DEFAULT_ERROR_MESSAGE,
                        -1,
                        ""
                    )
                )
            }
        }

        return Error(
            ErrorBody(
                DEFAULT_ERROR_MESSAGE,
                -1,
                ""
            )
        )
    }

    private fun <T : Any> handleResponseFailure(response: Response<T>): Error {
        val err = response.errorBody()
        if (err != null) {
            val errorDetail = err.string()
            // try getting first error detail
            return try {
                Error(
                    buildErrorModel(
                        response,
                        errorDetail
                    )
                )
                // Keeping old error flow intact
            } catch (ignore: Exception) {
                val responseCode = response.code()
                when {
                    responseCode >= HttpURLConnection.HTTP_BAD_REQUEST && responseCode < HttpURLConnection.HTTP_INTERNAL_ERROR -> Error(
                        ErrorBody(
                            "Unable to process your request. Please try again later.",
                            responseCode,
                            errorDetail
                        )
                    )
                    responseCode >= HttpURLConnection.HTTP_INTERNAL_ERROR && responseCode < HttpURLConnection.HTTP_INTERNAL_ERROR + 100 -> Error(
                        ErrorBody(
                            "Unable to process your request. Please try again later.",
                            responseCode,
                            errorDetail
                        )
                    )
                    else -> Error(
                        ErrorBody(
                            "Something went wrong! Please try again later",
                            responseCode,
                            errorDetail
                        )
                    )
                }
            }
        }

        return Error(
            ErrorBody(
                "Something went wrong! Please try again later",
                -1,
                ""
            )
        )
    }

    /**
     * Handle response error, default common error view converted, override it to change implementation
     *
     * @param response raw retrofit response
     * @param errorDetail error string from error body
     */
    private fun <T : Any> buildErrorModel(response: Response<T>, errorDetail: String): ErrorBody {

        if (errorDetail.contains("msg")) {
            val error = errorDetail.replace("msg", "message")
            return Gson().fromJson(error, ErrorBody::class.java)
                .copy(code = response.code(), rawMessage = errorDetail)
        }

        if (!errorDetail.contains("message")) {
            throw IllegalArgumentException()
        }

        // parse the error body from the response, copy the object apply the response code
        return Gson().fromJson(errorDetail, ErrorBody::class.java)
            .copy(code = response.code(), rawMessage = errorDetail)
    }
}