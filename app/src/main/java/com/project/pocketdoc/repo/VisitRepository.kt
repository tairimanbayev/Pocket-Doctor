package com.project.pocketdoc.repo

import android.content.Context
import com.project.pocketdoc.model.VisitPage
import com.project.pocketdoc.model.tables.Visit
import com.project.pocketdoc.model.tables.VisitCard
import com.project.pocketdoc.model.tables.VisitReason
import com.project.pocketdoc.network.requests.FcmRequest
import com.project.pocketdoc.util.PageParams
import com.project.pocketdoc.util.apiCall
import com.project.pocketdoc.util.apiCallResponse
import com.project.pocketdoc.util.dbCall


class VisitRepository(context: Context) : FcmRepository(context) {
    private val visitDao = database.visitDao
    private val cardDao = database.cardDao
    private val doctorDao = database.doctorDao
    private val profileDao = database.profileDao

    suspend fun getVisits(fcmId: String, pageParams: PageParams, onComplete: suspend (VisitPage) -> Unit) =
        apiCallResponse(
            { remoteApi.getVisitPage(fcmId, 4, pageParams.pageNumber * 4, pageParams.history, pageParams.active) },
            onComplete
        )

    suspend fun getVisits(fcmId: String, pageNumber: Int, history: Int, cardId: Int, onComplete: suspend (VisitPage) -> Unit) =
        apiCallResponse({ remoteApi.getVisitPageWithCardId(fcmId, 4, pageNumber * 4, history, cardId) }, onComplete)

    suspend fun finishVisit(visitId: Int, fcmRequest: FcmRequest, onComplete: suspend (Unit) -> Unit) =
        apiCall({ remoteApi.finishVisit(visitId, fcmRequest) }, onComplete)

    suspend fun acceptVisit(visitId: Int, fcmRequest: FcmRequest) =
        apiCall({ remoteApi.acceptVisit(visitId, fcmRequest) })

    suspend fun hideVisit(visitId: Int, fcmRequest: FcmRequest) =
        apiCall({ remoteApi.hideVisit(visitId, fcmRequest) })

    suspend fun getVisitWithCards(visitId: Int) = dbCall {
        visitDao.getVisit(visitId)?.also { visit ->
            visit.cards = cardDao.getCardsOfVisit(visitId) ?: emptyList()
            doctorDao.getDoctor(visit.doctorId ?: 0)?.let {
                it.profile = profileDao.getProfile(it.userId)?.apply {
                    if (cardId != null)
                        card = cardDao.getCard(cardId!!)
                }
                visit.doctor = it
            }
            visit.reasons = visitDao.getReasons(visitId) ?: emptyList()
        } ?: Visit()
    }

    suspend fun insert(visit: Visit) = dbCall {
        visitDao.insert(visit)
        insertVisitExtra(visit)
    }

    suspend fun insert(visits: List<Visit>) = dbCall {
        visitDao.insert(visits)
        for (visit in visits) {
            insertVisitExtra(visit)
        }
    }

    private suspend fun insertVisitExtra(visit: Visit) {
        cardDao.insert(visit.cards)
        for (card in visit.cards) {
            visitDao.insertVisitCards(VisitCard(visitId = visit.id, cardId = card.id))
        }
        val doctor = visit.doctor
        if (doctor != null) {
            doctorDao.insert(doctor)
            doctorDao.addClinic(doctor.clinic)
            doctor.profile?.let {
                doctor.userId = it.id
                profileDao.insert(it)
            }
        }
        visitDao.insertReasons(visit.reasons)
        visitDao.insertVisitReasons(visit.reasons.map { VisitReason(visitId = visit.id, reasonId = it.id) })
    }
}