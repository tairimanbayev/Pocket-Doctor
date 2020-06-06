package com.project.pocketdoc.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.pocketdoc.repo.PillRepository

@Suppress("UNCHECKED_CAST")
class PillsFactory(private val repository: PillRepository, private val cardId: Int) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PillsViewModel(repository, cardId) as T
    }
}