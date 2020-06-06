package com.project.pocketdoc.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.pocketdoc.repo.VisitRepository
import com.project.pocketdoc.viewmodels.VisitDetailedViewModel
import com.project.pocketdoc.viewmodels.VisitViewModel

@Suppress("UNCHECKED_CAST")
class VisitFactory(private val repository: VisitRepository, private val visitId: Int, private val detailed: Boolean) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return (if(detailed) VisitDetailedViewModel(
            repository,
            visitId
        ) else VisitViewModel(repository, visitId)) as T
    }
}