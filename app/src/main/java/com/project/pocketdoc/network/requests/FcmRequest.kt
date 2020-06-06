package com.project.pocketdoc.network.requests

import com.squareup.moshi.Json

data class FcmRequest(@field:Json(name = "fcm_id") var fcmId: String)