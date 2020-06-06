package com.project.pocketdoc.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.pocketdoc.repo.CardIllnessRepository
import com.project.pocketdoc.viewmodels.CardIllnessesViewModel
import com.project.pocketdoc.viewmodels.IllnessesViewModel

@Suppress("UNCHECKED_CAST")
class CardIllnessesFactory(
    private val repository: CardIllnessRepository,
    private val cardId: Int,
    private val visitId: Int,
    private val withCard: Boolean
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (withCard)
            CardIllnessesViewModel(repository, cardId, visitId) as T
        else
            IllnessesViewModel(repository, cardId, visitId) as T
    }
}