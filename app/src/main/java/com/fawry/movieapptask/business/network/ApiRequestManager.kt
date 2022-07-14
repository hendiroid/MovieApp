package com.fawry.movieapptask.business.network

import android.util.Log
import com.fawry.movieapptask.business.models.Message
import com.fawry.movieapptask.business.network.NetworkErrors.ERROR_UNKNOWN
import com.fawry.movieapptask.business.network.NetworkErrors.NETWORK_ERROR
import com.fawry.movieapptask.business.network.NetworkErrors.NETWORK_ERROR_TIMEOUT
import com.fawry.movieapptask.business.network.NetworkErrors.NETWORK_ERROR_UNKNOWN
import com.fawry.movieapptask.util.cLog
import com.google.gson.Gson
import kotlinx.coroutines.*
import okhttp3.Headers
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

interface ApiRequestManagerInterface {
    fun <T : Any> execute(
        request: suspend () -> Response<T>,
        onSuccess: ((T, Headers) -> Unit)? = null,
        onFailure: ((Message) -> Unit)? = null,
        finally: (() -> Unit)? = null
    ): Job
}

class ApiRequestManager() : ApiRequestManagerInterface {

    override fun <T : Any> execute(
        request: suspend () -> Response<T>,
        onSuccess: ((T, Headers) -> Unit)?,
        onFailure: ((Message) -> Unit)?,
        finally: (() -> Unit)?
    ): Job {
        return CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = request.invoke()
                val result = verifyResponse(response)

                withContext(Dispatchers.Main) {
                    when (result) {
                        is ResponseResult.Success -> onSuccess?.invoke(
                            result.data,
                            response.headers()
                        )
                        is ResponseResult.Failure -> onFailure?.invoke(result.message)
                    }
                }
            } catch (throwable: Throwable) {
                Log.e("requestException", throwable.message.toString())
                throwable.printStackTrace()
                when (throwable) {
                    is TimeoutCancellationException -> {
                        val code = 408 // timeout error code
                        ResponseResult.Failure(Message(message = NETWORK_ERROR_TIMEOUT))
                    }
                    is IOException -> {
                        ResponseResult.Failure(Message(message = NETWORK_ERROR))
                    }
                    is HttpException -> {
                        val code = throwable.code()
                        val errorResponse = convertErrorBody(throwable)
                        cLog(errorResponse)
                        ResponseResult.Failure(
                            Message(message = errorResponse!!)
                        )
                    }
                    else -> {
                        cLog(NETWORK_ERROR_UNKNOWN)
                        ResponseResult.Failure(
                            Message(message = NETWORK_ERROR_UNKNOWN)
                        )
                    }
                }
            } finally {
                withContext(Dispatchers.Main) {
                    finally?.invoke()
                }
            }
        }
    }

    private fun <T : Any> verifyResponse(response: Response<T>): ResponseResult<T> {
        return try {
            if (response.isSuccessful) {
                if (response.code() == 204) {
                    @Suppress("UNCHECKED_CAST")
                    (ResponseResult.Success(response.raw().message as T))
                } else {
                    ResponseResult.Success(response.body()!!)
                }
            } else {
                val message =
                    Gson().fromJson(response.errorBody()?.string(), Message::class.java)
                //  message.statusCode = response.code()
                ResponseResult.Failure(message)
            }
        } catch (ex: Exception) {
            ResponseResult.Failure(Message(message = ex.localizedMessage ?: ERROR_UNKNOWN))
        }
    }
}

private fun convertErrorBody(throwable: HttpException): String? {
    return try {
        throwable.response()?.errorBody()?.string()
    } catch (exception: Exception) {
        ERROR_UNKNOWN
    }
}


