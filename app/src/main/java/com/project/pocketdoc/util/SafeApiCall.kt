package com.project.pocketdoc.util

import android.util.Log
import com.project.pocketdoc.network.ResponseBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

val TAG = "Logcat"

suspend fun <T> apiCallResponse(call: suspend () -> ResponseBody<T>, onComplete: (suspend (T) -> Unit)? = null) =
    withContext(Dispatchers.IO) {
        try {
            val response = Status.Complete(call().body)
            onComplete?.invoke(response.result)
            response
        } catch (throwable: Throwable) {
            catchHandler(throwable)
        }
    }

suspend fun <T> apiCall(call: suspend () -> T, onComplete: (suspend (T) -> Unit)? = null) = withContext(Dispatchers.IO) {
    try {
        val response = Status.Complete(call())
        onComplete?.invoke(response.result)
        response
    } catch (throwable: Throwable) {
        catchHandler(throwable)
    }
}

private fun catchHandler(throwable: Throwable) = when(throwable) {
    is IOException -> Status.Failure(NETWORK_PROBLEM)
    is HttpException -> {
        val code = throwable.code()
        Log.d(TAG, "catchHandler: message: ${throwable.response()?.message()}")
        Log.d(TAG, "catchHandler: error body string: ${throwable.response()?.errorBody()?.string()}")
        Status.Failure(throwable.response()?.message() ?: getHttpErrorDescription(code))
    }
    else -> {
        throwable.printStackTrace()
        Status.Failure(throwable.localizedMessage ?: "Неизвестная проблема")
    }
}

suspend fun <T> dbCall(call: suspend () -> T) = withContext(Dispatchers.IO) { call() }

fun getHttpErrorDescription(code: Int) = when (code) {
    401 -> NOT_AUTHORIZED
    406 -> WRONG_CREDENTIALS
    422 -> UNACCEPTABLE_ENTITY
    else -> httpError(code)
}