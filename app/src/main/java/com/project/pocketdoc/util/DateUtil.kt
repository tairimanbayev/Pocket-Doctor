package com.project.pocketdoc.util

import java.text.DateFormatSymbols
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

fun getNotificationTimes(count: Int, period: Int): Array<Int> {
    val notificationTimes = ArrayList<Int>()
    notificationTimes.add(540)
    if (count > 1) {
        when (period) {
            1 -> {
                if (count == 3) notificationTimes.addAll(listOf(840, 1200))
                else {
                    for (i in 1 until count)
                        notificationTimes.add(550 + 640 * i / (count - 1))
                }
            }
            7 -> {
                when (count) {
                    2 -> notificationTimes.add(7740)
                    3 -> notificationTimes.addAll(listOf(4860, 7740))
                    else -> {
                        for (i in 1 until count)
                            notificationTimes.add(540 + 1440 * i)
                    }
                }
            }
            30 -> {
                when (count) {
                    2 -> notificationTimes.add(22140)
                    3 -> notificationTimes.addAll(listOf(14940, 29340))
                    else -> {
                        for (i in 1 until count)
                            notificationTimes.add(540 + i * 1440 * 30 / count)
                    }
                }
            }
        }
    }
    return notificationTimes.toTypedArray()
}

private val dateFormatSymbols = DateFormatSymbols().apply {
    months = arrayOf(
        "января",
        "февраля",
        "марта",
        "апреля",
        "мая",
        "июня",
        "июля",
        "августа",
        "сентября",
        "октября",
        "ноября",
        "декабря"
    )
}

val dateFormat = SimpleDateFormat("d MMMM yyyy", dateFormatSymbols)

fun reformat(date: String, pattern: String = "yyyy-MM-dd HH:mm:ss"): String? {
    val format = SimpleDateFormat(pattern, Locale.ENGLISH)
    return try {
        dateFormat.format(format.parse(date) ?: throw ParseException("", 0))
    } catch (e: ParseException) {
        null
    }
}