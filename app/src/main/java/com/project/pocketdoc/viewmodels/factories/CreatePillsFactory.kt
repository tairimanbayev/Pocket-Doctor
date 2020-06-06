package com.project.pocketdoc.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.pocketdoc.repo.PillRepository
import com.project.pocketdoc.viewmodels.CreatePillsViewModel

@Suppress("UNCHECKED_CAST")
class CreatePillsFactory(private val repository: PillRepository, private val cardId: Int, private val visitId: Int) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CreatePillsViewModel(repository, cardId, visitId) as T
    }
}