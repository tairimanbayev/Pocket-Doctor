package com.project.pocketdoc.model.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "reasons")
data class Reason(
    @PrimaryKey
    var id: Int = -1,
    var role: String = "",
    @ColumnInfo(name = "reason")
    @field:Json(name = "value")
    var reasonTitle: String = ""
)