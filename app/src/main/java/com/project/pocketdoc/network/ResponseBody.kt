package com.project.pocketdoc.network

import com.squareup.moshi.Json

data class ResponseBody<T>(
    @field:Json(name = "body")
    var body: T
)