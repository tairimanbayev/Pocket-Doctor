package com.project.pocketdoc.model.tables

import com.squareup.moshi.Json

class Pill(
    @field:Json(name = "id")
    var id: Int = 0,
    @field:Json(name = "illness_id")
    var illnessId: Int = 0,
    var name: String = "",
    var description: String = "",
    var period: Int = 0,
    @field:Json(name = "end_date")
    var endDate: String? = null,
    @field:Json(name = "fcm_id")
    var fcmId: String = "",
    var schedule: String = ""
) {
    fun getNotificationTime(): Array<Int> {
        if (schedule.length <= 2)
            return emptyArray<Int>()
        return schedule.substring(1 until schedule.length).split(",").map { it.toInt() }.toTypedArray()
    }

    fun setNotificationTime(array: Array<Int>) {
        val sb = StringBuilder()
        sb.append("[")
        for (int in array) {
            sb.append(int)
            sb.append(",")
        }
        sb.deleteCharAt(sb.length - 1)
        schedule = sb.toString()
    }
}