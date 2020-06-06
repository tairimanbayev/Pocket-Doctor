package com.project.pocketdoc.util

import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun getDescription(gender: Int, birthday: String): String {
    val age = calculateAge(birthday)
    var res = if (gender == 1) "Мужчина" else "Женщина"
    if (age != -1) {
        res += ", $age "
        res += when {
            ((age % 100) % 10 == 1) || (age % 10 > 4) -> "лет"
            age % 10 == 1 -> "год"
            else -> "года"
        }
    }
    return res
}

fun calculateAge(birthday: String): Int {
    val format = SimpleDateFormat("dd.MM.yyyy", Locale.US)
    return try {
        val date = format.parse(birthday)
        val birth = Calendar.getInstance().also { it.time = date }
        val today = Calendar.getInstance()

        var yearDiff = today.get(Calendar.YEAR) - birth.get(Calendar.YEAR)
        if (today.get(Calendar.MONTH) < birth.get(Calendar.MONTH) || today.get(Calendar.MONTH) == birth.get(Calendar.MONTH)
            && today.get(Calendar.DAY_OF_MONTH) < birth.get(Calendar.DAY_OF_MONTH))
            yearDiff--
        yearDiff
    } catch (e: ParseException) {
        e.printStackTrace()
        Log.d(TAG, "calculateAge: ${e.message}")
        -1
    }
}