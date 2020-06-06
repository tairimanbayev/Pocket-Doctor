package com.project.pocketdoc.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.pocketdoc.repo.VisitRepository
import com.project.pocketdoc.util.PageParams
import com.project.pocketdoc.viewmodels.VisitCurrentViewModel
import com.project.pocketdoc.viewmodels.VisitHistoryViewModel
import com.project.pocketdoc.viewmodels.VisitListViewModel

@Suppress("UNCHECKED_CAST")
class VisitListFactory(private val repository: VisitRepository, private val pageParams: PageParams) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>) = when {
        pageParams.checkActive() -> VisitListViewModel(repository, pageParams)
        pageParams.checkZeros() -> VisitCurrentViewModel(repository, pageParams)
        else -> VisitHistoryViewModel(repository, pageParams)
    } as T
}