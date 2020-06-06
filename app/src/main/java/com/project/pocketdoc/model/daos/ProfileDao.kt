package com.project.pocketdoc.model.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.project.pocketdoc.model.tables.Profile

@Dao
interface ProfileDao {
    @Query("SELECT * FROM profile WHERE id=:id")
    suspend fun getProfile(id: Int): Profile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profile: Profile)
}