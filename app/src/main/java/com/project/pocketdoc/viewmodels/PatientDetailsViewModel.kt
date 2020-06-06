package com.project.pocketdoc.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.project.pocketdoc.model.tables.Visit
import com.project.pocketdoc.network.requests.FcmRequest
import com.project.pocketdoc.repo.VisitRepository
import com.project.pocketdoc.util.FCM_PROBLEM
import com.project.pocketdoc.util.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PatientDetailsViewModel(private val repository: VisitRepository, private val visitId: Int) : StatusViewModel<Unit>() {

    private val _visit = MutableLiveData<Visit>()
    val visit: LiveData<Visit> by lazy {
        getVisit()
        _visit
    }

    fun getVisit() {
        viewModelScope.launch {
            _visit.value = repository.getVisitWithCards(visitId)
        }
    }

    fun finishVisit() {
        viewModelScope.launch {
            val fcm = repository.getFcm()
            if (fcm == null) {
                _status.value = Status.Failure(FCM_PROBLEM)
                return@launch
            }
            val fcmRequest = FcmRequest(fcm.fcmId)
            _status.value = repository.finishVisit(visit.value!!.id, fcmRequest) {
                withContext(Dispatchers.Main) {
                    _visit.value = _visit.value?.apply { finishedAt = "today" }
                }
            }
        }
    }
}

