package com.project.pocketdoc.model.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity
data class Card(
    @PrimaryKey
    var id: Int = 0,
    @ColumnInfo(name = "first_name") @field:Json(name = "first_name")
    var firstName: String = "",
    @ColumnInfo(name = "last_name") @field:Json(name = "last_name")
    var lastName: String = "",
    @ColumnInfo(name = "middle_name") @field:Json(name = "middle_name")
    var middleName: String? = "",
    var birthday: String = "",
    var gender: Int = 1,
    @ColumnInfo(name = "user_id") @field:Json(name = "user_id")
    var userId: Int = 0,
    @Ignore @field:Json(name = "user")
    var profile: Profile? = null,
    @ColumnInfo(name = "fcm_id") @field:Json(name = "fcm_id")
    var fcmId: String = "",
    var patronomic: String = "",
    var height: Int? = 0,
    var weight: Int? = 0,
    @field:Json(name = "hronics")
    var chronic: String? = null
)
