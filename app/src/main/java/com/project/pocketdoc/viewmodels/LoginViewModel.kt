package com.project.pocketdoc.viewmodels

import androidx.lifecycle.viewModelScope
import com.project.pocketdoc.network.requests.LoginRequest
import com.project.pocketdoc.repo.ProfileRepository
import com.project.pocketdoc.util.FCM_PROBLEM
import com.project.pocketdoc.util.Status
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: ProfileRepository) : LazyViewModel<Boolean>() {
    private val TAG = "LoginViewModelLogcat"

    override fun loadData() {
        viewModelScope.launch {
            if (repository.getLoggedProfileId() != -1) _status.value = Status.Complete(true)
        }
    }

    fun login(number: String, password: String) = viewModelScope.launch {
        val fcm = repository.getFcm()
        if (fcm == null) {
            _status.value = Status.Failure(FCM_PROBLEM)
            return@launch
        }
        val loginRequest = LoginRequest(number, password, fcm.fcmId)
        val result = repository.login(loginRequest) {
            repository.saveProfile(it)
        }
        _status.value = if (result is Status.Failure) result else {
            Status.Complete(true)
        }
    }
}