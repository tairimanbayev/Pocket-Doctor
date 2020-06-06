package com.project.pocketdoc.viewmodels

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.project.pocketdoc.model.VisitPage
import com.project.pocketdoc.repo.VisitRepository
import com.project.pocketdoc.util.FCM_PROBLEM
import com.project.pocketdoc.util.PageParams
import com.project.pocketdoc.util.Status
import kotlinx.coroutines.launch

class VisitHistoryViewModel(private val repository: VisitRepository, private val pageParams: PageParams) : LazyViewModel<VisitPage>() {
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
        Log.d("VisitHistoryVMLogcat", "loadMore: ")
        pageParams.pageNumber = pageNumber
        loadData()
    }
}

