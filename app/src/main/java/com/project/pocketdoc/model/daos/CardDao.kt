package com.project.pocketdoc.model.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.project.pocketdoc.model.tables.Card

@Dao
interface CardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(card: Card)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cards: List<Card>)

    @Query("SELECT * FROM card WHERE id = :id")
    suspend fun getCard(id: Int): Card?

    @Query("SELECT * FROM card WHERE id in (SELECT cardId FROM VisitCard WHERE visitId=:visitId)")
    suspend fun getCardsOfVisit(visitId: Int): List<Card>?
}