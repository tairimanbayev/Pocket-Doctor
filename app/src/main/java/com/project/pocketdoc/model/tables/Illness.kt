package com.project.pocketdoc.model.tables

import com.squareup.moshi.Json

data class Illness(
    var id: Int = 0,
    @field:Json(name = "card_id")
    var cardId: Int = 0,
    @field:Json(name = "title")
    var diagnosis: String = "",
    @field:Json(name = "description")
    var inspection: String = "",
    var complaint: String =  "",
    var appointment: String = "",
    var result: String = "",
    @field:Json(name = "fcm_id")
    var fcmId: String = "",
    var card: Card = Card()
)