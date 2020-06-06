package com.project.pocketdoc.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.pocketdoc.repo.VisitRepository
import com.project.pocketdoc.util.PageParams
import com.project.pocketdoc.viewmodels.HistoryViewModel

@Suppress("UNCHECKED_CAST")
class HistoryFactory(private val repository: VisitRepository, private val pageParams: PageParams) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HistoryViewModel(repository, pageParams) as T
    }
}