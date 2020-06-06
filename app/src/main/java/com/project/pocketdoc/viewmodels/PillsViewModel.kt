package com.project.pocketdoc.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.project.pocketdoc.model.tables.Card
import com.project.pocketdoc.model.tables.Pill
import com.project.pocketdoc.repo.PillRepository
import com.project.pocketdoc.util.FCM_PROBLEM
import com.project.pocketdoc.util.Status
import kotlinx.coroutines.launch

class PillsViewModel(private val repository: PillRepository, private val cardId: Int) : LazyViewModel<List<Pill>>() {

    private val _card = MutableLiveData<Card>()
    val card: LiveData<Card> by lazy {
        viewModelScope.launch { _card.value = repository.getCard(cardId) }
        _card
    }

    override fun loadData() {
        viewModelScope.launch {
            _status.value = Status.Loading
            val fcm = repository.getFcm()
            _status.value = if (fcm == null)
                Status.Failure(FCM_PROBLEM)
            else repository.getPillsList(cardId, fcm.fcmId)
        }
    }
}

