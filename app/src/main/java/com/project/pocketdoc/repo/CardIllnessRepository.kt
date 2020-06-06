package com.project.pocketdoc.repo

import android.content.Context
import com.project.pocketdoc.model.tables.Card
import com.project.pocketdoc.model.tables.Illness
import com.project.pocketdoc.network.requests.FcmRequest
import com.project.pocketdoc.util.apiCall
import com.project.pocketdoc.util.apiCallResponse
import com.project.pocketdoc.util.dbCall

class CardIllnessRepository(context: Context) : FcmRepository(context) {
    private val cardDao = database.cardDao

    suspend fun getCardFromDB(cardId: Int) = dbCall { cardDao.getCard(cardId) }

    suspend fun saveCardToDB(card: Card) = dbCall { cardDao.insert(card) }

    suspend fun updateCard(card: Card, onComplete: suspend (Card) -> Unit) =
        apiCallResponse({ remoteApi.updateCard(card.id, card) }, onComplete)

    suspend fun getIllnessList(cardId: Int, visitId: Int, fcmId: String, onComplete: (suspend (List<Illness>) -> Unit)? = null) =
        apiCallResponse({ remoteApi.getIllnessList(cardId, visitId, fcmId) }, onComplete)

    suspend fun createIllness(visitId: Int, illness: Illness) =
        apiCallResponse({ remoteApi.createIllness(visitId, illness) })

    suspend fun updateIllness(illness: Illness) =
        apiCallResponse({ remoteApi.updateIllness(illness.id, illness) })

    suspend fun deleteIllness(illnessId: Int, fcmRequest: FcmRequest) =
        apiCall({ remoteApi.deleteIllness(illnessId, fcmRequest) })
}
