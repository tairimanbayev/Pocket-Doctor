package com.project.pocketdoc.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.pocketdoc.model.tables.Visit
import com.project.pocketdoc.repo.VisitRepository
import kotlinx.coroutines.launch

class VisitViewModel(private val repository: VisitRepository, private val visitId: Int) : ViewModel() {

    private val _visit = MutableLiveData<Visit>()
    val visit: LiveData<Visit> by lazy {
        viewModelScope.launch {
            _visit.value = repository.getVisitWithCards(visitId)
        }
        _visit
    }
}