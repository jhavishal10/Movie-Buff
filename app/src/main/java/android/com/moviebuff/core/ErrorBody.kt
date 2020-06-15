package android.com.moviebuff.core

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.net.HttpURLConnection

data class ErrorBody(val message: String, val code: Int = -1, val rawMessage: String) {

    /**
     * Return value for given key if match found in only the FIRST LEVEL of the raw JSON
     * else return null
     */
    fun getValueFromRaw(key: String): String? {
        return try {
            val parser = JsonParser()
            val json: JsonObject = parser.parse(rawMessage).asJsonObject
            json.get(key).toString()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

const val DEFAULT_ERROR_MESSAGE = "Something went wrong. Please try again later."

const val ERROR_CODE_UNKNOWN_HOST = 450
const val ERROR_CODE_CANCELLATION_JOB = 451
const val ERROR_CODE_TIMEOUT = HttpURLConnection.HTTP_CLIENT_TIMEOUT
const val ERROR_CODE_IO_EXCEPTION = 452
const val ERROR_CODE_TOKEN_EXPIRED = 449