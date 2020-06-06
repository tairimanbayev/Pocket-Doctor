package com.project.pocketdoc.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.project.pocketdoc.model.tables.Card
import com.project.pocketdoc.model.tables.Illness
import com.project.pocketdoc.model.tables.Pill
import com.project.pocketdoc.network.requests.FcmRequest
import com.project.pocketdoc.repo.PillRepository
import com.project.pocketdoc.util.FCM_PROBLEM
import com.project.pocketdoc.util.Status
import kotlinx.coroutines.launch

class CreatePillsViewModel(private val repository: PillRepository, private val cardId: Int, private val visitId: Int) :
    LazyViewModel<List<Pill>>() {

    private val _card = MutableLiveData<Card>()
    val card: LiveData<Card> by lazy {
        viewModelScope.launch {
            _card.value = repository.getCard(cardId)
        }
        return@lazy _card
    }

    private val _illnessList = MutableLiveData<Status<List<Illness>>>()
    val illnessList: LiveData<Status<List<Illness>>>
        get() = _illnessList

    private val _update = MutableLiveData<Status<Unit>>()
    val update: LiveData<Status<Unit>>
        get() = _update

    override fun loadData() {
        viewModelScope.launch {
            val fcm = repository.getFcm()
            _status.value = if (fcm == null)
                Status.Failure(FCM_PROBLEM)
            else {
                loadIllnesses()
                repository.getPillsList(cardId, visitId, fcm.fcmId)
            }
        }
    }

    fun loadIllnesses() {
        viewModelScope.launch {
            val fcm = repository.getFcm()
            if (fcm != null)
                _illnessList.value = repository.getIllnessList(cardId, visitId, fcm.fcmId)
        }
    }

    fun updatePills(items: ArrayList<Pill>, deletedItems: ArrayList<Pill>) {
        viewModelScope.launch {
            val fcm = repository.getFcm()
            if (fcm == null) {
                _update.value = Status.Failure(FCM_PROBLEM)
                return@launch
            }
            var failed = false
            for (item in items) {
                item.fcmId = fcm.fcmId
                val result = if (item.id == 0) repository.createPill(item) else repository.updatePill(item)
                if (!failed && result is Status.Failure) {
                    failed = true
                    _update.value = result
                }
            }
            deletedItems.filter { it.id != 0 }.forEach { item ->
                val fcmRequest = FcmRequest(fcm.fcmId)
                val result = repository.deletePill(item.id, fcmRequest)
                if (!failed && result is Status.Failure) {
                    failed = true
                    _update.value = result
                }
            }
            if (!failed)
                _update.value = Status.Complete(Unit)
        }
    }
}

