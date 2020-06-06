package com.project.pocketdoc.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.pocketdoc.repo.VisitRepository
import com.project.pocketdoc.viewmodels.PatientDetailsViewModel

@Suppress("UNCHECKED_CAST")
class PatientDetailsFactory(private val repository: VisitRepository, private val visitId: Int) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PatientDetailsViewModel(repository, visitId) as T
    }
}