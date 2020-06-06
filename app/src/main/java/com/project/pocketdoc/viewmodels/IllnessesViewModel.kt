package com.project.pocketdoc.viewmodels

import androidx.lifecycle.viewModelScope
import com.project.pocketdoc.model.tables.Card
import com.project.pocketdoc.model.tables.Illness
import com.project.pocketdoc.repo.CardIllnessRepository
import com.project.pocketdoc.util.FCM_PROBLEM
import com.project.pocketdoc.util.Status
import kotlinx.coroutines.launch

class IllnessesViewModel(private val repository: CardIllnessRepository, private val cardId: Int, private val visitId: Int) :
    LazyViewModel<List<Illness>>() {

    override fun loadData() {
        _status.value = Status.Loading
        viewModelScope.launch {
            val fcm = repository.getFcm()
            _status.value = if (fcm == null)
                Status.Failure(FCM_PROBLEM)
            else {
                repository.getIllnessList(cardId, visitId, fcm.fcmId) { illnesses ->
                    val card = repository.getCardFromDB(cardId)
                    illnesses.forEach { it.card = card ?: Card() }
                }
            }
        }
    }
}