package com.project.pocketdoc.model.tables

import androidx.room.*
import com.squareup.moshi.Json

@Entity
data class Address(
    @PrimaryKey
    var id: Int = 0,
    @ColumnInfo(name = "user_id") @field:Json(name = "user_id")
    var profileId: Int? = null,
    @Ignore @field:Json(name = "user")
    var profile: Profile? = null,
    var city: String? = null,
    var street: String? = null,
    var house: String? = null,
    var corpus: String? = null,
    var block: String? = null,
    var floor: String? = null,
    var flat: String? = null,
    @ColumnInfo(name = "code") @field:Json(name = "code")
    var doorCode: String? = null,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    @ColumnInfo(name = "fcm_id") @field:Json(name = "fcm_id")
    var fcmId: String = ""
)

data class VisitsToAddress(
    @Embedded
    var address: Address,
    @Relation(parentColumn = "id", entityColumn = "address_id", entity = Visit::class)
    var visits: List<Visit>
)