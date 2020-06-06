package com.project.pocketdoc.viewmodels

import androidx.lifecycle.viewModelScope
import com.project.pocketdoc.model.VisitPage
import com.project.pocketdoc.repo.VisitRepository
import com.project.pocketdoc.util.FCM_PROBLEM
import com.project.pocketdoc.util.PageParams
import com.project.pocketdoc.util.Status
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: VisitRepository, private val pageParams: PageParams) :
    LazyViewModel<VisitPage>() {
    override fun loadData() {
        viewModelScope.launch {
            val fcm = repository.getFcm()
            _status.value = if (fcm == null)
                Status.Failure(FCM_PROBLEM)
            else repository.getVisits(fcm.fcmId, pageParams.pageNumber, pageParams.history, pageParams.active) {
                it.visits.forEach { visit -> visit.cards.forEach { card -> card.fcmId = fcm.fcmId } }
            }
        }
    }

    fun loadMore(pageNumber: Int) {
        pageParams.pageNumber = pageNumber
        loadData()
    }
}

