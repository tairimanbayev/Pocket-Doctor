package com.project.pocketdoc.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.project.pocketdoc.model.tables.Visit
import com.project.pocketdoc.network.requests.FcmRequest
import com.project.pocketdoc.repo.VisitRepository
import com.project.pocketdoc.util.FCM_PROBLEM
import com.project.pocketdoc.util.Status
import kotlinx.coroutines.launch

class VisitDetailedViewModel(private val repository: VisitRepository, private val visitId: Int) :
    StatusViewModel<Boolean>() {

    private val _visit = MutableLiveData<Visit>()
    val visit: LiveData<Visit> by lazy {
        viewModelScope.launch { _visit.value = repository.getVisitWithCards(visitId) }
        _visit
    }

    fun clicked(accept: Boolean) = viewModelScope.launch {
        val fcm = repository.getFcm()
        _status.value = if (fcm == null) {
            Status.Failure(FCM_PROBLEM)
        } else {
            val fcmRequest = FcmRequest(fcm.fcmId)
            val result =
                if (accept) repository.acceptVisit(visitId, fcmRequest) else repository.hideVisit(visitId, fcmRequest)
            if (result is Status.Failure) result else Status.Complete(accept)
        }
    }
}

