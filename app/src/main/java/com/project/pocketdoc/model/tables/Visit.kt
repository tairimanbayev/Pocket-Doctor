package com.project.pocketdoc.model.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity
data class Visit(
    @PrimaryKey
    var id: Int = 0,
    @ColumnInfo(name = "fcm_id") @field:Json(name = "fcm_id")
    var fcmId: String = "",
    var role: String = "",
    @ColumnInfo(name = "address_id") @field:Json(name = "address_id")
    var addressId: Int = 0,
    @Ignore @field:Json(name = "address")
    var address: Address? = null,
    @ColumnInfo(name = "payment_card_id") @field:Json(name = "payment_card_id")
    var paymentCardId: Int? = null,
    @ColumnInfo(name = "doctor_id") @field:Json(name = "doctor_id")
    var doctorId: Int? = 0,
    @Ignore @field:Json(name = "doctor")
    var doctor: Doctor? = null,
    @ColumnInfo(name = "visit_at") @field:Json(name = "visit_at")
    var date: String = "",
    @ColumnInfo(name = "finished_at") @field:Json(name = "finished_at")
    var finishedAt: String? = null,
    var comment: String? = null,
    @Ignore @field:Json(name = "questions")
    var reasons: List<Reason> = emptyList(),
    @Ignore @field:Json(name = "card_id")
    var cardId: List<Int> = emptyList(),
    @Ignore @field:Json(name = "cards")
    var cards: List<Card> = emptyList(),
    var price: Double? = null
)

@Entity
data class VisitCard(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var visitId: Int,
    var cardId: Int
)

@Entity
data class VisitReason(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var visitId: Int,
    var reasonId: Int
)