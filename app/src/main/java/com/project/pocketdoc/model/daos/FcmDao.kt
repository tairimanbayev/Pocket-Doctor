package com.project.pocketdoc.model.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.project.pocketdoc.model.tables.Fcm

@Dao
interface FcmDao {
    @Query("SELECT * FROM fcm LIMIT 1")
    suspend fun getFcm() : Fcm?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(fcm: Fcm)
}