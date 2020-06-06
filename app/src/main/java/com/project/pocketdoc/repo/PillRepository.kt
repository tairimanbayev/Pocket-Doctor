package com.project.pocketdoc.repo

import android.content.Context
import com.project.pocketdoc.model.tables.Pill
import com.project.pocketdoc.network.requests.FcmRequest
import com.project.pocketdoc.util.apiCall
import com.project.pocketdoc.util.apiCallResponse
import com.project.pocketdoc.util.dbCall

class PillRepository(private val context: Context) : FcmRepository(context) {
    private val cardDao = database.cardDao

    suspend fun getPillsList(cardId: Int, visitId: Int, fcmId: String) =
        apiCallResponse({ remoteApi.getPillList(cardId, visitId, fcmId) })

    suspend fun getPillsList(cardId: Int, fcmId: String) =
        apiCallResponse({ remoteApi.getPillList(cardId, fcmId) })

    suspend fun createPill(pill: Pill) =
        apiCall({ remoteApi.createPill(pill) })

    suspend fun updatePill(pill: Pill) =
        apiCall({ remoteApi.updatePill(pill.id, pill) })

    suspend fun deletePill(pillId: Int, fcmRequest: FcmRequest) =
        apiCall({ remoteApi.deletePill(pillId, fcmRequest) })

    suspend fun getIllnessList(cardId: Int, visitId: Int, fcmId: String) =
        apiCallResponse({ remoteApi.getIllnessList(cardId, visitId, fcmId) })

    suspend fun getCard(cardId: Int) = dbCall { cardDao.getCard(cardId) }
}