package com.project.pocketdoc.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.pocketdoc.repo.ProfileRepository
import com.project.pocketdoc.viewmodels.EditProfileViewModel

@Suppress("UNCHECKED_CAST")
class EditProfileFactory(private val repository: ProfileRepository, private val cardId: Int) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EditProfileViewModel(repository, cardId) as T
    }
}