package com.project.pocketdoc.model.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity
data class Profile(
    @field:Json(name = "phone_number") @ColumnInfo(name = "phone_number")
    var phoneNumber: String = "",
    var email: String? = "",
    @PrimaryKey
    var id: Int = 0,
    @field:Json(name = "card_id") @ColumnInfo(name = "card_id")
    var cardId: Int? = 0,
    @field:Json(name = "doctor_id") @ColumnInfo(name = "doctor_id")
    var doctorId: Int? = 0,
    @ColumnInfo(name = "fcm_id") @field:Json(name = "fcm_id")
    var fcmId: String = "",

    @Ignore @field:Json(name = "card")
    var card: Card? = null,
    @Ignore @field:Json(name = "doctor")
    var doctor: Doctor? = null
)