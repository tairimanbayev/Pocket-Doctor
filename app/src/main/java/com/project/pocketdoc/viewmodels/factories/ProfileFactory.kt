package com.project.pocketdoc.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.pocketdoc.repo.ProfileRepository
import com.project.pocketdoc.viewmodels.LoginViewModel
import com.project.pocketdoc.viewmodels.ProfileViewModel

@Suppress("UNCHECKED_CAST")
class ProfileFactory(private val repository: ProfileRepository, private val isLogin: Boolean) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (isLogin) LoginViewModel(repository) as T else ProfileViewModel(
            repository
        ) as T
    }
}