package com.project.pocketdoc.network.requests

import com.squareup.moshi.Json


data class LoginRequest (
    @field:Json(name = "phone_number")
    var phoneNumber: String = "",
    var token: String = "",
    @field:Json(name = "fcm_id")
    var fcmId: String = ""
)