package com.project.pocketdoc.viewmodels

import androidx.lifecycle.viewModelScope
import com.project.pocketdoc.model.VisitPage
import com.project.pocketdoc.repo.VisitRepository
import com.project.pocketdoc.util.FCM_PROBLEM
import com.project.pocketdoc.util.PageParams
import com.project.pocketdoc.util.Status
import kotlinx.coroutines.launch

class VisitListViewModel(private val repository: VisitRepository, val pageParams: PageParams) : LazyViewModel<VisitPage>() {
    override fun loadData() {
        viewModelScope.launch {
            val fcm = repository.getFcm()
            _status.value = if (fcm == null)
                Status.Failure(FCM_PROBLEM)
            else repository.getVisits(fcm.fcmId, pageParams) {
                for (i in it.visits)
                    for (j in i.cards)
                        j.fcmId = fcm.fcmId
                launch { repository.insert(it.visits) }
            }
        }
    }

    fun loadMore(pageNumber: Int) {
        pageParams.pageNumber = pageNumber
        loadData()
    }
}

