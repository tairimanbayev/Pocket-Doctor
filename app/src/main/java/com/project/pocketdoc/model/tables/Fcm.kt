package com.project.pocketdoc.model.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Fcm(
    @PrimaryKey
    var id: Int = 0,
    @ColumnInfo(name = "fcm_id")
    var fcmId: String = ""
)