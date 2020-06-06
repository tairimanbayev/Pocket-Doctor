package com.project.pocketdoc.util

sealed class Status<out T> {
    data class Complete<out T>(val result: T) : Status<T>()
    data class Failure(val message: String) : Status<Nothing>()
    object Loading : Status<Nothing>()
}

const val NETWORK_PROBLEM = "Не удалось подключиться к сети"
const val FCM_PROBLEM = "Проблемы с сервером"
const val NOT_AUTHORIZED = "Пользователь не авторизован"
const val WRONG_CREDENTIALS = "Данные не верны"
const val UNACCEPTABLE_ENTITY = "Необрабатываемый запрос"

fun httpError(code: Int) = "Ошибка №$code"
