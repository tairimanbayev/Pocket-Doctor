package com.project.pocketdoc.model.tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Post (
    @PrimaryKey
    var id: Int = -1,
    var userId: Int = -1,
    var title: String = "",
    var body: String = ""
)