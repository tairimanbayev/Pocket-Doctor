package com.project.pocketdoc.model.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.project.pocketdoc.model.tables.Reason
import com.project.pocketdoc.model.tables.Visit
import com.project.pocketdoc.model.tables.VisitCard
import com.project.pocketdoc.model.tables.VisitReason

@Dao
interface VisitDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(visit: Visit)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(visits: List<Visit>)

    @Query("DELETE FROM visit")
    suspend fun clearTable()

    @Query("SELECT * FROM visit WHERE id=:visitId")
    suspend fun getVisit(visitId: Int): Visit?

    @Query("SELECT * FROM visit")
    suspend fun getVisits(): List<Visit>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisitCards(vc: VisitCard)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReasons(reasons: List<Reason>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisitReasons(vr: List<VisitReason>)

    @Query("SELECT * FROM reasons WHERE id IN (SELECT reasonId FROM visitreason WHERE visitId = :visitId)")
    suspend fun getReasons(visitId: Int): List<Reason>?
}