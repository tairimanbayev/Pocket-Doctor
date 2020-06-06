package com.project.pocketdoc.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.project.pocketdoc.model.tables.Card
import com.project.pocketdoc.model.tables.Illness
import com.project.pocketdoc.network.requests.FcmRequest
import com.project.pocketdoc.repo.CardIllnessRepository
import com.project.pocketdoc.util.FCM_PROBLEM
import com.project.pocketdoc.util.SingleLiveEvent
import com.project.pocketdoc.util.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CardIllnessesViewModel(
    private val repository: CardIllnessRepository,
    private val cardId: Int,
    private val visitId: Int
) : LazyViewModel<List<Illness>>() {

    private val _card = MutableLiveData<Card>()
    val card: LiveData<Card> by lazy {
        viewModelScope.launch { _card.value = repository.getCardFromDB(cardId) }
        return@lazy _card
    }

    private val _updateResult = SingleLiveEvent<Status<Boolean>>()
    val updateResult: LiveData<Status<Boolean>>
        get() = _updateResult

    override fun loadData() {
        viewModelScope.launch {
            val fcm = repository.getFcm()
            _status.value = if (fcm == null)
                Status.Failure(FCM_PROBLEM)
            else
                repository.getIllnessList(cardId, visitId, fcm.fcmId)
        }
    }

    fun saveChanges(height: Int, weight: Int, chronic: String, items: ArrayList<Illness>, deletedItems: ArrayList<Illness>) {
        viewModelScope.launch {
            val fcm = repository.getFcm()
            if (fcm == null) {
                _updateResult.value = Status.Failure(FCM_PROBLEM)
                return@launch
            }
            card.value?.let {
                it.height = height
                it.weight = weight
                it.chronic = chronic
                it.fcmId = fcm.fcmId
            }
            val response = repository.updateCard(card.value!!) {
                repository.saveCardToDB(it)
                updateIllnesses(items, deletedItems, fcm.fcmId)
            }
            if (response is Status.Failure)
                _updateResult.value = response
        }
    }

    private suspend fun updateIllnesses(items: ArrayList<Illness>, deletedItems: ArrayList<Illness>, fcmId: String) {
        var failed = false
        for (illness in items) {
            illness.fcmId = fcmId
            val response = if (illness.id == 0) {
                illness.cardId = cardId
                repository.createIllness(visitId, illness)
            } else
                repository.updateIllness(illness)
            if (!failed && response is Status.Failure) {
                failed = true
                _updateResult.value = response
            }
        }
        deletedItems.filter { it.id != 0 }.forEach { illness ->
            val fcmRequest = FcmRequest(fcmId)
            val result = repository.deleteIllness(illness.id, fcmRequest)
            if (!failed && result is Status.Failure) {
                failed = true
                _updateResult.value = result
            }
        }
        if (!failed)
            withContext(Dispatchers.Main) { _updateResult.value = Status.Complete(true) }
    }
}

