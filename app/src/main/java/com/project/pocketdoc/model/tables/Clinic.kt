package com.project.pocketdoc.model.tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Clinic(
    @PrimaryKey
    var id: Int = 0,
    var name: String = ""
)